/**
 * Copyright 2011-2015 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.gen.proxy;

import com.sun.codemodel.*;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.analysis.astAnalyzer.AOPProxyAspect;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.aop.MethodInterceptorChain;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.ClassNamer;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.validation.Validator;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.*;


/**
 * @author John Ericksen
 */
public class AOPProxyGenerator {

    private static final String SUPER_REF = "super";
    private static final String CLASS_GET_METHOD = "getMethod";
    private static final String AOPPROXY_EXT = "AOPProxy";
    private static final String METHOD_INTERCEPTOR_INVOKE = "invoke";

    private final UniqueVariableNamer variableNamer;
    private final ClassNamer classNamer;
    private final ClassGenerationUtil generationUtil;
    private final Validator validator;

    @Inject
    public AOPProxyGenerator(UniqueVariableNamer variableNamer, ClassNamer classNamer, ClassGenerationUtil generationUtil, Validator validator) {
        this.variableNamer = variableNamer;
        this.classNamer = classNamer;
        this.generationUtil = generationUtil;
        this.validator = validator;
    }

    public InjectionNode generateProxy(InjectionNode injectionNode) {
        AOPProxyAspect aopProxyAspect = injectionNode.getAspect(AOPProxyAspect.class);
        JDefinedClass definedClass;

        ASTInjectionAspect injectionAspect = injectionNode.getAspect(ASTInjectionAspect.class);
        ConstructorInjectionPoint constructorInjectionPoint = injectionAspect.getConstructorInjectionPoint();
        ConstructorInjectionPoint proxyConstructorInjectionPoint = new ConstructorInjectionPoint(injectionNode.getASTType(), constructorInjectionPoint.getConstructor());

        try {
            PackageClass aopClassName = classNamer.numberedClassName(injectionNode.getASTType().getPackageClass())
                    .namespaced()
                    .append(AOPPROXY_EXT)
                    .build();

            definedClass = generationUtil.defineClass(aopClassName);
            definedClass.annotate(SuppressWarnings.class).param("value", "unchecked");

            //extending injectionNode
            definedClass._extends(generationUtil.ref(injectionNode.getASTType()));

            //copy constructor elements and add aop interceptors
            JMethod constructor = definedClass.constructor(JMod.PUBLIC);

            //converting exceptions into runtime exceptions
            proxyConstructorInjectionPoint.addThrows(constructorInjectionPoint.getThrowsTypes());
            for (ASTType throwType : constructorInjectionPoint.getThrowsTypes()) {
                constructor._throws(generationUtil.ref(throwType));
            }

            JBlock constructorBody = constructor.body();

            List<JVar> superArguments = new ArrayList<JVar>();
            for (InjectionNode node : constructorInjectionPoint.getInjectionNodes()) {
                String paramName = variableNamer.generateName(node);
                JVar param = constructor.param(generationUtil.ref(node.getASTType()), paramName);
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
            throw new TransfuseAnalysisException("JClassAlreadyExistsException while building AOP Proxy", e);
        }

        return buildProxyInjectionNode(injectionNode, definedClass.fullName(), injectionAspect, proxyConstructorInjectionPoint);
    }

    private InjectionNode buildProxyInjectionNode(InjectionNode injectionNode, String proxyClassName, ASTInjectionAspect injectionAspect, ConstructorInjectionPoint proxyConstructorInjectionPoint) {
        InjectionNode proxyInjectionNode = new InjectionNode(new InjectionSignature(
                new ASTProxyType(injectionNode.getASTType(), proxyClassName)));

        proxyInjectionNode.getAspects().putAll(injectionNode.getAspects());

        //alter construction injection
        ASTInjectionAspect proxyInjectionAspect = new ASTInjectionAspect();
        proxyInjectionAspect.addAllInjectionGroups(injectionAspect.getGroups());
        //replace proxy constructor because of optional interceptor construction parameters
        proxyInjectionAspect.set(proxyConstructorInjectionPoint);

        proxyInjectionAspect.setAssignmentType(injectionAspect.getAssignmentType());

        proxyInjectionNode.addAspect(proxyInjectionAspect);

        return proxyInjectionNode;
    }

    private void buildMethodInterceptor(JDefinedClass definedClass, ConstructorInjectionPoint proxyConstructorInjectionPoint, JMethod constructor, JBlock constructorBody, Map<ASTMethod, Map<InjectionNode, JFieldVar>> interceptorFields, Map.Entry<ASTMethod, Set<InjectionNode>> methodInterceptorEntry) {
        ASTMethod method = methodInterceptorEntry.getKey();

        if (method.getAccessModifier().equals(ASTAccessModifier.PRIVATE)) {
            validator.error("AOP Method Interception is unavailable on private methods")
                                           .element(method).build();
            return;
        }

        if (!interceptorFields.containsKey(methodInterceptorEntry.getKey())) {
            interceptorFields.put(methodInterceptorEntry.getKey(), new HashMap<InjectionNode, JFieldVar>());
        }
        Map<InjectionNode, JFieldVar> injectionNodeInstanceNameMap = interceptorFields.get(methodInterceptorEntry.getKey());

        //setup interceptor fields
        for (InjectionNode interceptorInjectionNode : methodInterceptorEntry.getValue()) {
            String interceptorInstanceName = variableNamer.generateName(interceptorInjectionNode);

            JFieldVar interceptorField = definedClass.field(JMod.PRIVATE, generationUtil.ref(interceptorInjectionNode.getASTType()), interceptorInstanceName);

            injectionNodeInstanceNameMap.put(interceptorInjectionNode, interceptorField);

            JVar interceptorParam = constructor.param(generationUtil.ref(interceptorInjectionNode.getASTType()), variableNamer.generateName(interceptorInjectionNode));

            constructorBody.assign(interceptorField, interceptorParam);

            proxyConstructorInjectionPoint.addInjectionNode(interceptorInjectionNode);
        }

        JType returnType = generationUtil.type(method.getReturnType());

        JMethod methodDeclaration = definedClass.method(method.getAccessModifier().getCodeModelJMod(), returnType, method.getName());
        methodDeclaration.annotate(Override.class);
        JBlock body = methodDeclaration.body();

        //define method parameter
        Map<ASTParameter, JVar> parameterMap = new HashMap<ASTParameter, JVar>();
        for (ASTParameter parameter : method.getParameters()) {
            parameterMap.put(parameter,
                    methodDeclaration.param(JMod.FINAL, generationUtil.ref(parameter.getASTType()),
                            variableNamer.generateName(parameter.getASTType())));
        }

        //aop interceptor
        Map<InjectionNode, JFieldVar> interceptorNameMap = interceptorFields.get(methodInterceptorEntry.getKey());

        JArray paramArray = JExpr.newArray(generationUtil.ref(Object.class));

        for (ASTParameter astParameter : method.getParameters()) {
            paramArray.add(parameterMap.get(astParameter));
        }

        JInvocation interceptorInvocation = buildInterceptorChain(definedClass, method, parameterMap, methodInterceptorEntry.getValue(), interceptorNameMap).invoke(METHOD_INTERCEPTOR_INVOKE);
        interceptorInvocation.arg(paramArray);

        if (method.getReturnType().equals(ASTVoidType.VOID)) {
            body.add(interceptorInvocation);
        } else {
            body._return(JExpr.cast(returnType.boxify(), interceptorInvocation));
        }
    }

    private JExpression buildInterceptorChain(JDefinedClass definedClass, ASTMethod method, Map<ASTParameter, JVar> parameterMap, Set<InjectionNode> interceptors, Map<InjectionNode, JFieldVar> interceptorNameMap) {

        try {
            JDefinedClass methodExecutionClass = definedClass._class(JMod.PRIVATE | JMod.FINAL, classNamer.numberedClassName(MethodInterceptorChain.MethodExecution.class).build().getClassName());
            methodExecutionClass._implements(MethodInterceptorChain.MethodExecution.class);

            //setup constructor with needed parameters
            JMethod constructor = methodExecutionClass.constructor(JMod.PUBLIC);
            JBlock constructorBody = constructor.body();
            List<JExpression> methodParameters = new ArrayList<JExpression>();
            for (ASTParameter parameter : method.getParameters()) {
                JType parameterType = parameterMap.get(parameter).type();
                JVar param = constructor.param(parameterType, variableNamer.generateName(parameterType));
                JFieldVar field = methodExecutionClass.field(JMod.PRIVATE, parameterType, variableNamer.generateName(parameterType));
                constructorBody.assign(field, param);
                methodParameters.add(field);
            }


            //getMethod()
            JMethod getMethod = methodExecutionClass.method(JMod.PUBLIC, Method.class, MethodInterceptorChain.MethodExecution.GET_METHOD);
            getMethod.annotate(Override.class);

            JInvocation getMethodInvocation = definedClass.dotclass().invoke(CLASS_GET_METHOD).arg(method.getName());
            getMethod.body()._return(getMethodInvocation);
            getMethod._throws(NoSuchMethodException.class);

            for (ASTParameter astParameter : method.getParameters()) {
                getMethodInvocation.arg(generationUtil.ref(astParameter.getASTType()).dotclass());
            }

            //invoke()
            JMethod invokeMethod = methodExecutionClass.method(JMod.PUBLIC, Object.class, MethodInterceptorChain.MethodExecution.INVOKE);
            invokeMethod.annotate(Override.class);

            //add all throws of contained method
            for (ASTType throwable : method.getThrowsTypes()) {
                invokeMethod._throws(generationUtil.ref(throwable));
            }

            JInvocation superCall = definedClass.staticRef(SUPER_REF).invoke(method.getName());

            for (JExpression methodParam : methodParameters) {
                superCall.arg(methodParam);
            }

            if (method.getReturnType().equals(ASTVoidType.VOID)) {
                invokeMethod.body().add(superCall);
                invokeMethod.body()._return(JExpr._null());
            } else {
                invokeMethod.body()._return(superCall);
            }

            JInvocation methodExecutionInvocation = JExpr._new(methodExecutionClass);

            for (ASTParameter astParameter : method.getParameters()) {
                methodExecutionInvocation.arg(parameterMap.get(astParameter));
            }

            JInvocation newInterceptorInvocation = JExpr._new(generationUtil.ref(MethodInterceptorChain.class))
                    .arg(methodExecutionInvocation)
                    .arg(JExpr._this());

            for (InjectionNode interceptor : interceptors) {
                newInterceptorInvocation.arg(interceptorNameMap.get(interceptor));
            }

            return newInterceptorInvocation;
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Class already defined while generating inner class", e);
        }
    }
}
