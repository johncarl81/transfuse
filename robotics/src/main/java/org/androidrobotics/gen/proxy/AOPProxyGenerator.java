package org.androidrobotics.gen.proxy;

import com.sun.codemodel.*;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.adapter.*;
import org.androidrobotics.analysis.astAnalyzer.AOPProxyAspect;
import org.androidrobotics.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidrobotics.aop.MethodInvocation;
import org.androidrobotics.gen.UniqueVariableNamer;
import org.androidrobotics.model.ConstructorInjectionPoint;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.MethodInjectionPoint;
import org.androidrobotics.util.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

/**
 * @author John Ericksen
 */
@Singleton
public class AOPProxyGenerator {

    private static final String INVOKE_METHOD = "invoke";
    private static final String PROCEED_METHOD = "proceed";
    private static final String SUPER_REF = "super";

    private JCodeModel codeModel;
    private UniqueVariableNamer variableNamer;
    private Map<String, InjectionNode> aopProxiesGenerated = new HashMap<String, InjectionNode>();
    private Logger logger;

    @Inject
    public AOPProxyGenerator(JCodeModel codeModel, UniqueVariableNamer variableNamer, Logger logger) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
        this.logger = logger;
    }

    public InjectionNode generateProxy(InjectionNode injectionNode) {
        if (!aopProxiesGenerated.containsKey(injectionNode.getClassName())) {
            InjectionNode proxyInjectionNode = innerGenerateProxyCode(injectionNode);
            aopProxiesGenerated.put(injectionNode.getClassName(), proxyInjectionNode);
        }

        return aopProxiesGenerated.get(injectionNode.getClassName());
    }

    public InjectionNode innerGenerateProxyCode(InjectionNode injectionNode) {
        AOPProxyAspect aopProxyAspect = injectionNode.getAspect(AOPProxyAspect.class);
        JDefinedClass definedClass;
        String proxyClassName = injectionNode.getClassName() + "_AOPProxy";
        ASTInjectionAspect injectionAspect = injectionNode.getAspect(ASTInjectionAspect.class);
        ConstructorInjectionPoint constructorInjectionPoint = injectionAspect.getConstructorInjectionPoint();
        ConstructorInjectionPoint proxyConstructorInjectionPoint = new ConstructorInjectionPoint(ASTAccessModifier.PUBLIC);

        try {

            definedClass = codeModel._class(JMod.PUBLIC, proxyClassName, ClassType.CLASS);
            //extending injectionNode
            definedClass._extends(codeModel.ref(injectionNode.getClassName()));

            //copy constructor elements and add aop interceptors
            JMethod constructor = definedClass.constructor(JMod.PUBLIC);

            JBlock constructorBody = constructor.body();

            List<JVar> superArguments = new ArrayList<JVar>();
            for (InjectionNode node : constructorInjectionPoint.getInjectionNodes()) {
                String paramName = variableNamer.generateName(node.getClassName());
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
        }

        return buildProxyInjectionNode(injectionNode, proxyClassName, injectionAspect, proxyConstructorInjectionPoint);
    }

    private void buildMethodInterceptor(JDefinedClass definedClass, ConstructorInjectionPoint proxyConstructorInjectionPoint, JMethod constructor, JBlock constructorBody, Map<ASTMethod, Map<InjectionNode, JFieldVar>> interceptorFields, Map.Entry<ASTMethod, Set<InjectionNode>> methodInterceptorEntry) {
        ASTMethod method = methodInterceptorEntry.getKey();

        if (method.getAccessModifier() == ASTAccessModifier.PRIVATE) {
            throw new RoboticsAnalysisException("Unable to provide AOP on private methods");
        }

        if (!interceptorFields.containsKey(methodInterceptorEntry.getKey())) {
            interceptorFields.put(methodInterceptorEntry.getKey(), new HashMap<InjectionNode, JFieldVar>());
        }
        Map<InjectionNode, JFieldVar> injectionNodeInstanceNameMap = interceptorFields.get(methodInterceptorEntry.getKey());

        for (InjectionNode interceptorInjectionNode : methodInterceptorEntry.getValue()) {
            String interceptorInstanceName = variableNamer.generateName(interceptorInjectionNode.getClassName());


            JFieldVar interceptorField = definedClass.field(JMod.PRIVATE, codeModel.ref(interceptorInjectionNode.getClassName()), interceptorInstanceName);

            injectionNodeInstanceNameMap.put(interceptorInjectionNode, interceptorField);

            JVar interceptorParam = constructor.param(codeModel.ref(interceptorInjectionNode.getClassName()), variableNamer.generateName(interceptorInjectionNode.getClassName()));

            constructorBody.assign(interceptorField, interceptorParam);

            proxyConstructorInjectionPoint.addInjectionNode(interceptorInjectionNode);
        }

        JType returnType;
        if (method.getReturnType().equals(ASTVoidType.VOID)) {
            returnType = codeModel.VOID;
        } else {
            returnType = codeModel.ref(method.getReturnType().getName());
        }
        JMethod methodDeclaration = definedClass.method(method.getAccessModifier().getCodeModelJMod(), returnType, method.getName());
        JBlock body = methodDeclaration.body();

        //define method parameter
        Map<ASTParameter, JVar> parameterMap = new HashMap<ASTParameter, JVar>();
        for (ASTParameter parameter : method.getParameters()) {
            parameterMap.put(parameter,
                    methodDeclaration.param(JMod.FINAL, codeModel.ref(parameter.getASTType().getName()),
                            variableNamer.generateName(parameter.getASTType().getName())));
        }

        //aop interceptor
        JTryBlock tryInterceptorBlock = body._try();
        JBlock tryInterceptorBody = tryInterceptorBlock.body();


        Iterator<InjectionNode> interceptorIterator = methodInterceptorEntry.getValue().iterator();

        Map<InjectionNode, JFieldVar> interceptorNameMap = interceptorFields.get(methodInterceptorEntry.getKey());
        JInvocation invocation = buildInterceptorChain(interceptorIterator, interceptorNameMap, parameterMap, method, definedClass, tryInterceptorBody);

        if (method.getReturnType().equals(ASTVoidType.VOID)) {
            tryInterceptorBody.add(invocation);
        } else {
            tryInterceptorBody._return(JExpr.cast(codeModel.ref(method.getReturnType().getName()), invocation));
        }

        JCatchBlock interceptorCatchBlock = tryInterceptorBlock._catch(codeModel.ref(Throwable.class));
        JVar throwableE = interceptorCatchBlock.param("e");
        JBlock throwableCatchBody = interceptorCatchBlock.body();

        throwableCatchBody._throw(JExpr._new(codeModel.ref(RoboticsAnalysisException.class)).arg(JExpr.lit("exception during method interception")).arg(throwableE));
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

        proxyInjectionNode.addAspect(proxyInjectionAspect);

        return proxyInjectionNode;
    }

    private JInvocation buildInterceptorChain(Iterator<InjectionNode> interceptorIterator, Map<InjectionNode, JFieldVar> interceptorFieldMap, Map<ASTParameter, JVar> parameterMap, ASTMethod method, JDefinedClass enclosingClass, JBlock body) {
        if (interceptorIterator.hasNext()) {
            InjectionNode interceptorInjectionNode = interceptorIterator.next();

            JFieldVar interceptorField = interceptorFieldMap.get(interceptorInjectionNode);

            JDefinedClass innerMethodInvocation = codeModel.anonymousClass(codeModel.ref(MethodInvocation.class));

            JMethod proceedMethod = innerMethodInvocation.method(JMod.PUBLIC, codeModel.ref(Object.class), PROCEED_METHOD);

            JBlock proceedBody = proceedMethod.body();

            buildInterceptorChain(interceptorIterator, interceptorFieldMap, parameterMap, method, enclosingClass, proceedBody);

            return interceptorField.invoke(INVOKE_METHOD).arg(
                    JExpr._new(innerMethodInvocation)
            );
        } else {
            JInvocation superCall = enclosingClass.staticRef(SUPER_REF).invoke(method.getName());

            for (ASTParameter parameter : method.getParameters()) {
                superCall.arg(parameterMap.get(parameter));
            }

            if (method.getReturnType().equals(ASTVoidType.VOID)) {
                body.add(superCall);
                body._return(JExpr._null());

            } else {
                body._return(superCall);
            }

            return null;
        }
    }
}
