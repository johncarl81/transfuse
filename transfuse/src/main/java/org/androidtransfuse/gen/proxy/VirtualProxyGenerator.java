package org.androidtransfuse.gen.proxy;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.*;
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
//todo: move away from singleton
@Singleton
public class VirtualProxyGenerator {

    private static final String DELEGATE_NAME = "delegate";
    private static final String DELEGATE_LOAD_METHOD_PARAM_NAME = "delegateInput";
    private static final String CHECK_DELEGATE = "checkDelegate";
    private static final String PROXY_NOT_INITIALIZED = "Trying to use a proxied instance before initialization";
    private static final String VPROXY_EXT = "_VProxy";

    private final JCodeModel codeModel;
    private final UniqueVariableNamer variableNamer;
    private final ASTClassFactory astClassFactory;
    private final ClassGenerationUtil generationUtil;
    private final Map<ASTType, JDefinedClass> descriptorCache = new HashMap<ASTType, JDefinedClass>();

    @Inject
    public VirtualProxyGenerator(JCodeModel codeModel, UniqueVariableNamer variableNamer, ASTClassFactory astClassFactory, ClassGenerationUtil generationUtil) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
        this.astClassFactory = astClassFactory;
        this.generationUtil = generationUtil;
    }

    public JDefinedClass generateProxy(InjectionNode injectionNode) {
        if (!descriptorCache.containsKey(injectionNode.getASTType())) {
            descriptorCache.put(injectionNode.getASTType(), innerGenerateProxy(injectionNode));
        }
        return descriptorCache.get(injectionNode.getASTType());
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
            returnType = codeModel.ref(method.getReturnType().getName());
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
