/**
 * Copyright 2013 John Ericksen
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
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.DelayedLoad;
import org.androidtransfuse.util.VirtualProxyException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * @author John Ericksen
 */
public class VirtualProxyGenerator {

    private static final String DELEGATE_NAME = "delegate";
    private static final String DELEGATE_LOAD_METHOD_PARAM_NAME = "delegateInput";
    private static final String CHECK_DELEGATE = "checkDelegate";
    private static final String PROXY_NOT_INITIALIZED = "Trying to use a proxied instance before initialization";
    private static final String VPROXY_EXT = "$VProxy";

    private final JCodeModel codeModel;
    private final UniqueVariableNamer variableNamer;
    private final ASTClassFactory astClassFactory;
    private final ClassGenerationUtil generationUtil;
    private final VirtualProxyGeneratorCache cache;

    @Singleton
    public static class VirtualProxyGeneratorCache {

        private final Map<ASTType, JDefinedClass> descriptorCache = new HashMap<ASTType, JDefinedClass>();

        public synchronized JDefinedClass getCached(InjectionNode injectionNode, VirtualProxyGenerator generator) {
            if (!descriptorCache.containsKey(injectionNode.getASTType())) {
                descriptorCache.put(injectionNode.getASTType(), generator.innerGenerateProxy(injectionNode));
            }
            return descriptorCache.get(injectionNode.getASTType());
        }
    }

    @Inject
    public VirtualProxyGenerator(JCodeModel codeModel, UniqueVariableNamer variableNamer, ASTClassFactory astClassFactory, ClassGenerationUtil generationUtil, VirtualProxyGeneratorCache cache) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
        this.astClassFactory = astClassFactory;
        this.generationUtil = generationUtil;
        this.cache = cache;
    }

    public JDefinedClass generateProxy(InjectionNode injectionNode) {
        return cache.getCached(injectionNode, this);
    }

    public JDefinedClass innerGenerateProxy(InjectionNode injectionNode) {

        try {

            JDefinedClass definedClass = generationUtil.defineClass(injectionNode.getASTType().getPackageClass().append(VPROXY_EXT));

            //define delegate
            JClass delegateClass = codeModel.ref(injectionNode.getClassName());

            Set<ASTType> proxyInterfaces = injectionNode.getAspect(VirtualProxyAspect.class).getProxyInterfaces();

            JFieldVar delegateField = definedClass.field(JMod.PRIVATE, delegateClass, DELEGATE_NAME,
                    JExpr._null());

            definedClass._implements(codeModel.ref(DelayedLoad.class).narrow(delegateClass));

            JMethod delayedLoadMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, DelayedLoad.LOAD_METHOD);
            delayedLoadMethod.annotate(Override.class);
            JVar delegateParam = delayedLoadMethod.param(delegateClass, DELEGATE_LOAD_METHOD_PARAM_NAME);
            delayedLoadMethod.body().assign(delegateField, delegateParam);

            JMethod delegateCheckMethod = definedClass.method(JMod.PRIVATE, codeModel.VOID, CHECK_DELEGATE);
            JBlock delegateNullBlock = delegateCheckMethod.body()._if(delegateField.eq(JExpr._null()))._then();

            delegateNullBlock._throw(JExpr._new(codeModel.ref(VirtualProxyException.class)).arg(PROXY_NOT_INITIALIZED));


            Set<MethodSignature> methodSignatures = new HashSet<MethodSignature>();

            //implements interfaces
            if (injectionNode.containsAspect(VirtualProxyAspect.class)) {
                for (ASTType interfaceType : proxyInterfaces) {
                    definedClass._implements(codeModel.ref(interfaceType.getName()));

                    //implement methods
                    for (ASTMethod method : interfaceType.getMethods()) {
                        //checking uniqueness
                        if (methodSignatures.add(new MethodSignature(method))) {
                            buildProxyMethod(definedClass, delegateField, delegateCheckMethod, method);
                        }
                    }
                }

                //equals, hashcode, and toString()
                ASTMethod equals = getASTMethod("equals", Object.class);
                ASTMethod hashCode = getASTMethod("hashCode");
                ASTMethod toString = getASTMethod("toString");
                if (methodSignatures.add(new MethodSignature(equals))) {
                    buildProxyMethod(definedClass, delegateField, delegateCheckMethod, equals);
                }

                if (methodSignatures.add(new MethodSignature(hashCode))) {
                    buildProxyMethod(definedClass, delegateField, delegateCheckMethod, hashCode);
                }

                if (methodSignatures.add(new MethodSignature(toString))) {
                    buildProxyMethod(definedClass, delegateField, delegateCheckMethod, toString);
                }

            }

            return definedClass;
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Error while trying to build new class", e);
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("Unable to find expected method", e);
        }
    }

    private void buildProxyMethod(JDefinedClass definedClass, JFieldVar delegateField, JMethod checkDelegateMethod, ASTMethod method) {
        // public <type> <method_name> ( <parameters...>)
        JType returnType;
        if (method.getReturnType() != null) {
            //todo: fix this "no type"
            if(method.getReturnType().getClass().equals(ASTEmptyType.class)){
                returnType = codeModel.ref(Object.class);
            }
            else{
                returnType = codeModel.ref(method.getReturnType().getName());
            }
        } else {
            returnType = codeModel.VOID;
        }
        JMethod methodDeclaration = definedClass.method(JMod.PUBLIC, returnType, method.getName());

        //define method parameter
        Map<ASTParameter, JVar> parameterMap = new HashMap<ASTParameter, JVar>();
        for (ASTParameter parameter : method.getParameters()) {
            parameterMap.put(parameter,
                    methodDeclaration.param(codeModel.ref(parameter.getASTType().getName()),
                            variableNamer.generateName(parameter.getASTType())));
        }

        //define method body
        JBlock body = methodDeclaration.body();

        body.invoke(checkDelegateMethod);

        //delegate invocation
        JInvocation invocation = delegateField.invoke(method.getName());

        for (ASTParameter parameter : method.getParameters()) {
            invocation.arg(parameterMap.get(parameter));
        }

        if (method.getReturnType().equals(ASTVoidType.VOID)) {
            body.add(invocation);
        } else {
            body._return(invocation);
        }
    }

    private ASTMethod getASTMethod(String name, Class... parameters) throws NoSuchMethodException {
        Method method = Object.class.getMethod(name, parameters);
        return astClassFactory.getMethod(method);
    }

    public TypedExpression initializeProxy(InjectionBuilderContext context, TypedExpression proxyVariable, TypedExpression variableBuilder) {

        context.getBlock().add(
                proxyVariable.getExpression().invoke(DelayedLoad.LOAD_METHOD).arg(variableBuilder.getExpression()));

        return variableBuilder;
    }
}
