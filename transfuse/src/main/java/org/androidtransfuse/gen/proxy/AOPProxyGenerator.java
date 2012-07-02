package org.androidtransfuse.gen.proxy;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.*;
import org.androidtransfuse.analysis.astAnalyzer.AOPProxyAspect;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.aop.MethodInterceptorChain;
import org.androidtransfuse.gen.GeneratedClassAnnotator;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author John Ericksen
 */
@Singleton
public class AOPProxyGenerator {

    private static final String SUPER_REF = "super";
    private static final String CLASS_GET_METHOD = "getMethod";

    private JCodeModel codeModel;
    private UniqueVariableNamer variableNamer;
    private Map<String, InjectionNode> aopProxiesGenerated = new HashMap<String, InjectionNode>();
    private Logger logger;
    private GeneratedClassAnnotator generatedClassAnnotator;

    @Inject
    public AOPProxyGenerator(JCodeModel codeModel, UniqueVariableNamer variableNamer, Logger logger, GeneratedClassAnnotator generatedClassAnnotator) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
        this.logger = logger;
        this.generatedClassAnnotator = generatedClassAnnotator;
    }

    public InjectionNode generateProxy(InjectionNode injectionNode) {
        if (!aopProxiesGenerated.containsKey(injectionNode.getClassName())) {
            InjectionNode proxyInjectionNode = innerGenerateProxyCode(injectionNode);
            aopProxiesGenerated.put(injectionNode.getClassName(), proxyInjectionNode);
        }

        return aopProxiesGenerated.get(injectionNode.getClassName());
    }

    private InjectionNode innerGenerateProxyCode(InjectionNode injectionNode) {
        AOPProxyAspect aopProxyAspect = injectionNode.getAspect(AOPProxyAspect.class);
        JDefinedClass definedClass;
        String proxyClassName = injectionNode.getClassName() + "_AOPProxy";
        ASTInjectionAspect injectionAspect = injectionNode.getAspect(ASTInjectionAspect.class);
        ConstructorInjectionPoint constructorInjectionPoint = injectionAspect.getConstructorInjectionPoint();
        ConstructorInjectionPoint proxyConstructorInjectionPoint = new ConstructorInjectionPoint(ASTAccessModifier.PUBLIC);

        try {

            definedClass = codeModel._class(JMod.PUBLIC, proxyClassName, ClassType.CLASS);

            generatedClassAnnotator.annotateClass(definedClass);

            //extending injectionNode
            definedClass._extends(codeModel.ref(injectionNode.getClassName()));

            //copy constructor elements and add aop interceptors
            JMethod constructor = definedClass.constructor(JMod.PUBLIC);

            //converting exceptions into runtime exceptions
            proxyConstructorInjectionPoint.addThrows(constructorInjectionPoint.getThrowsTypes());
            for (ASTType throwType : constructorInjectionPoint.getThrowsTypes()) {
                constructor._throws(codeModel.ref(throwType.getName()));
            }

            JBlock constructorBody = constructor.body();

            List<JVar> superArguments = new ArrayList<JVar>();
            for (InjectionNode node : constructorInjectionPoint.getInjectionNodes()) {
                String paramName = variableNamer.generateName(node);
                JVar param = constructor.param(codeModel.ref(node.getClassName()), paramName);
                superArguments.add(param);
                proxyConstructorInjectionPoint.addInjectionNode(node);
            }

            //super construction
            JInvocation constructorInvocation = constructorBody.invoke(SUPER_REF);
            for (JVar paramArgument : superArguments) {
                constructorInvocation.arg(paramArgument);
            }

            //method interceptors
            Map<ASTMethod, Map<InjectionNode, JFieldVar>> interceptorFields = new HashMap<ASTMethod, Map<InjectionNode, JFieldVar>>();
            for (Map.Entry<ASTMethod, Set<InjectionNode>> methodInterceptorEntry : aopProxyAspect.getMethodInterceptors().entrySet()) {

                buildMethodInterceptor(definedClass, proxyConstructorInjectionPoint, constructor, constructorBody, interceptorFields, methodInterceptorEntry);
            }

        } catch (JClassAlreadyExistsException e) {
            logger.error("JClassAlreadyExistsException while building AOP Proxy", e);
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException while building AOP Proxy", e);
        }

        return buildProxyInjectionNode(injectionNode, proxyClassName, injectionAspect, proxyConstructorInjectionPoint);
    }

    private InjectionNode buildProxyInjectionNode(InjectionNode injectionNode, String proxyClassName, ASTInjectionAspect injectionAspect, ConstructorInjectionPoint proxyConstructorInjectionPoint) {
        InjectionNode proxyInjectionNode = new InjectionNode(
                new ASTProxyType(injectionNode.getASTType(), proxyClassName));

        proxyInjectionNode.getAspects().putAll(injectionNode.getAspects());

        //alter construction injection
        ASTInjectionAspect proxyInjectionAspect = new ASTInjectionAspect();
        //flag InjectionUtil that it needs to set the super class' fields
        for (FieldInjectionPoint fieldInjectionPoint : injectionAspect.getFieldInjectionPoints()) {
            fieldInjectionPoint.setProxied(true);
        }
        proxyInjectionAspect.addAllFieldInjectionPoints(injectionAspect.getFieldInjectionPoints());
        for (MethodInjectionPoint methodInjectionPoint : injectionAspect.getMethodInjectionPoints()) {
            methodInjectionPoint.setProxied(true);
        }
        proxyInjectionAspect.addAllMethodInjectionPoints(injectionAspect.getMethodInjectionPoints());
        //replace proxy constructor because of optional interceptor construction parameters
        proxyInjectionAspect.add(proxyConstructorInjectionPoint);

        proxyInjectionAspect.setAssignmentType(injectionAspect.getAssignmentType());

        proxyInjectionNode.addAspect(proxyInjectionAspect);

        return proxyInjectionNode;
    }

    private void buildMethodInterceptor(JDefinedClass definedClass, ConstructorInjectionPoint proxyConstructorInjectionPoint, JMethod constructor, JBlock constructorBody, Map<ASTMethod, Map<InjectionNode, JFieldVar>> interceptorFields, Map.Entry<ASTMethod, Set<InjectionNode>> methodInterceptorEntry) throws ClassNotFoundException {
        ASTMethod method = methodInterceptorEntry.getKey();

        if (method.getAccessModifier().equals(ASTAccessModifier.PRIVATE)) {
            throw new TransfuseAnalysisException("Unable to provide AOP on private methods");
        }

        if (!interceptorFields.containsKey(methodInterceptorEntry.getKey())) {
            interceptorFields.put(methodInterceptorEntry.getKey(), new HashMap<InjectionNode, JFieldVar>());
        }
        Map<InjectionNode, JFieldVar> injectionNodeInstanceNameMap = interceptorFields.get(methodInterceptorEntry.getKey());

        //setup interceptor fields
        for (InjectionNode interceptorInjectionNode : methodInterceptorEntry.getValue()) {
            String interceptorInstanceName = variableNamer.generateName(interceptorInjectionNode);

            JFieldVar interceptorField = definedClass.field(JMod.PRIVATE, codeModel.ref(interceptorInjectionNode.getClassName()), interceptorInstanceName);

            injectionNodeInstanceNameMap.put(interceptorInjectionNode, interceptorField);

            JVar interceptorParam = constructor.param(codeModel.ref(interceptorInjectionNode.getClassName()), variableNamer.generateName(interceptorInjectionNode));

            constructorBody.assign(interceptorField, interceptorParam);

            proxyConstructorInjectionPoint.addInjectionNode(interceptorInjectionNode);
        }

        JType returnType = codeModel.parseType(method.getReturnType().getName());

        JMethod methodDeclaration = definedClass.method(method.getAccessModifier().getCodeModelJMod(), returnType, method.getName());
        JBlock body = methodDeclaration.body();

        //define method parameter
        Map<ASTParameter, JVar> parameterMap = new HashMap<ASTParameter, JVar>();
        for (ASTParameter parameter : method.getParameters()) {
            parameterMap.put(parameter,
                    methodDeclaration.param(JMod.FINAL, codeModel.ref(parameter.getASTType().getName()),
                            variableNamer.generateName(parameter.getASTType())));
        }

        //aop interceptor
        Map<InjectionNode, JFieldVar> interceptorNameMap = interceptorFields.get(methodInterceptorEntry.getKey());

        JArray paramArray = JExpr.newArray(codeModel.ref(Object.class));

        for (ASTParameter astParameter : method.getParameters()) {
            paramArray.add(parameterMap.get(astParameter));
        }

        JInvocation interceptorInvocation = buildInterceptorChain(definedClass, method, parameterMap, methodInterceptorEntry.getValue(), interceptorNameMap).invoke("invoke");
        interceptorInvocation.arg(paramArray);

        if (method.getReturnType().equals(ASTVoidType.VOID)) {
            body.add(interceptorInvocation);
        } else {
            body._return(JExpr.cast(returnType.boxify(), interceptorInvocation));
        }
    }

    private JExpression buildInterceptorChain(JDefinedClass definedClass, ASTMethod method, Map<ASTParameter, JVar> parameterMap, Set<InjectionNode> interceptors, Map<InjectionNode, JFieldVar> interceptorNameMap) {

        //todo:move into named inner class
        JDefinedClass methodExecutionClass = codeModel.anonymousClass(MethodInterceptorChain.MethodExecution.class);

        //getMethod()
        JMethod getMethod = methodExecutionClass.method(JMod.PUBLIC, Method.class, MethodInterceptorChain.MethodExecution.GET_METHOD);

        JInvocation getMethodInvocation = definedClass.dotclass().invoke(CLASS_GET_METHOD).arg(method.getName());
        getMethod.body()._return(getMethodInvocation);
        getMethod._throws(NoSuchMethodException.class);

        for (ASTParameter astParameter : method.getParameters()) {
            getMethodInvocation.arg(codeModel.ref(astParameter.getASTType().getName()).dotclass());
        }

        //invoke()
        JMethod invokeMethod = methodExecutionClass.method(JMod.PUBLIC, Object.class, MethodInterceptorChain.MethodExecution.INVOKE);

        //add all throws of contained method
        for (ASTType throwable : method.getThrowsTypes()) {
            invokeMethod._throws(codeModel.ref(throwable.getName()));
        }

        JInvocation superCall = definedClass.staticRef(SUPER_REF).invoke(method.getName());

        for (ASTParameter parameter : method.getParameters()) {
            superCall.arg(parameterMap.get(parameter));
        }

        if (method.getReturnType().equals(ASTVoidType.VOID)) {
            invokeMethod.body().add(superCall);
            invokeMethod.body()._return(JExpr._null());
        } else {
            invokeMethod.body()._return(superCall);
        }

        JInvocation newInterceptorInvocation = JExpr._new(codeModel.ref(MethodInterceptorChain.class))
                .arg(JExpr._new(methodExecutionClass))
                .arg(JExpr._this());

        for (InjectionNode interceptor : interceptors) {
            newInterceptorInvocation.arg(interceptorNameMap.get(interceptor));
        }

        return newInterceptorInvocation;
    }
}
