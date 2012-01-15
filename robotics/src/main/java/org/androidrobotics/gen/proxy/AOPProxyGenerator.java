package org.androidrobotics.gen.proxy;

import com.sun.codemodel.*;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.adapter.ASTAccessModifier;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTParameter;
import org.androidrobotics.analysis.adapter.ASTProxyType;
import org.androidrobotics.analysis.astAnalyzer.AOPProxyAspect;
import org.androidrobotics.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidrobotics.aop.MethodInvocation;
import org.androidrobotics.gen.UniqueVariableNamer;
import org.androidrobotics.model.ConstructorInjectionPoint;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.MethodInjectionPoint;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

/**
 * @author John Ericksen
 */
@Singleton
public class AOPProxyGenerator {

    private static final String VOID_TYPE_NAME = "void";
    private static final String INVOKE_METHOD = "invoke";
    private static final String PROCEED_METHOD = "proceed";
    private static final String SUPER_REF = "super";

    private JCodeModel codeModel;
    private UniqueVariableNamer variableNamer;
    private Map<String, InjectionNode> aopProxiesGenerated = new HashMap<String, InjectionNode>();

    @Inject
    public AOPProxyGenerator(JCodeModel codeModel, UniqueVariableNamer variableNamer) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
    }

    public InjectionNode generateProxy(InjectionNode injectionNode) {
        if (!aopProxiesGenerated.containsKey(injectionNode.getClassName())) {
            InjectionNode proxyInjectionNode = innerGenerateProxyCode(injectionNode);
            aopProxiesGenerated.put(injectionNode.getClassName(), proxyInjectionNode);
        }

        return aopProxiesGenerated.get(injectionNode.getClassName());
    }

    public InjectionNode innerGenerateProxyCode(InjectionNode injectionNode) {
        InjectionNode proxyInjectionNode = null;
        AOPProxyAspect aopProxyAspect = injectionNode.getAspect(AOPProxyAspect.class);
        JDefinedClass definedClass = null;
        try {
            String proxyClassName = injectionNode.getClassName() + "_AOPProxy";
            definedClass = codeModel._class(JMod.PUBLIC, proxyClassName, ClassType.CLASS);
            //extending injectionNode
            definedClass._extends(codeModel.ref(injectionNode.getClassName()));

            //copy constructor and add aop interceptors
            JMethod constructor = definedClass.constructor(JMod.PUBLIC);
            ConstructorInjectionPoint proxyConstructorInjectionPoint = new ConstructorInjectionPoint(ASTAccessModifier.PUBLIC);

            ASTInjectionAspect injectionAspect = injectionNode.getAspect(ASTInjectionAspect.class);
            ConstructorInjectionPoint constructorInjectionPoint = injectionAspect.getConstructorInjectionPoint();

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
            }

            for (Map.Entry<ASTMethod, Set<InjectionNode>> methodInterceptorEntry : aopProxyAspect.getMethodInterceptors().entrySet()) {
                ASTMethod method = methodInterceptorEntry.getKey();

                if (method.getAccessModifier() == ASTAccessModifier.PRIVATE) {
                    throw new RoboticsAnalysisException("Unable to provide AOP on private methods");
                }

                JType returnType;
                if (method.getReturnType() != null) {
                    returnType = codeModel.ref(method.getReturnType().getName());
                } else {
                    returnType = codeModel.VOID;
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

                //todo:fix void and primitive return
                if (method.getReturnType() == null || VOID_TYPE_NAME.equals(method.getReturnType().getName())) {
                    tryInterceptorBody.add(invocation);
                } else {
                    tryInterceptorBody._return(JExpr.cast(codeModel.ref(method.getReturnType().getName()), invocation));
                }

                JCatchBlock interceptorCatchBlock = tryInterceptorBlock._catch(codeModel.ref(Throwable.class));
                JVar throwableE = interceptorCatchBlock.param("e");
                JBlock throwableCatchBody = interceptorCatchBlock.body();

                throwableCatchBody._throw(JExpr._new(codeModel.ref(RoboticsAnalysisException.class)).arg(JExpr.lit("exception during method interception")).arg(throwableE));
            }

            proxyInjectionNode = new InjectionNode(
                    new ASTProxyType(injectionNode.getAstType(), proxyClassName));

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

        } catch (JClassAlreadyExistsException e) {
            e.printStackTrace();
        }


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

            //todo:fix void and primitive return
            if (method.getReturnType() == null || VOID_TYPE_NAME.equals(method.getReturnType().getName())) {
                body.add(superCall);
                body._return(JExpr._null());

            } else {
                body._return(superCall);
            }

            return null;
        }
    }
}
