package org.androidtransfuse.gen.proxy;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.adapter.ASTVoidType;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidtransfuse.gen.GeneratedClassAnnotator;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.ProxyDescriptor;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.MethodSignature;
import org.androidtransfuse.util.VirtualProxyException;

import javax.inject.Inject;
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
    protected static final String DELAYED_LOAD_METHOD_NAME = "load";
    private static final String PROXY_NOT_INITIALIZED = "Trying to use a proxied instance before initialization";

    private JCodeModel codeModel;
    private UniqueVariableNamer variableNamer;
    private GeneratedClassAnnotator generatedClassAnnotator;

    @Inject
    public VirtualProxyGenerator(JCodeModel codeModel, UniqueVariableNamer variableNamer, GeneratedClassAnnotator generatedClassAnnotator) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
        this.generatedClassAnnotator = generatedClassAnnotator;
    }

    public ProxyDescriptor generateProxy(InjectionNode injectionNode) {

        try {

            JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, injectionNode.getClassName() + "_VProxy", ClassType.CLASS);

            generatedClassAnnotator.annotateClass(definedClass);

            //define delegate
            JClass delegateClass = codeModel.ref(injectionNode.getClassName());

            Set<ASTType> proxyInterfaces = injectionNode.getAspect(VirtualProxyAspect.class).getProxyInterfaces();

            JFieldVar delegateField = definedClass.field(JMod.PRIVATE, delegateClass, DELEGATE_NAME,
                    JExpr._null());

            definedClass._implements(codeModel.ref(DelayedLoad.class).narrow(delegateClass));

            JMethod delayedLoadMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, DELAYED_LOAD_METHOD_NAME);
            JVar delegateParam = delayedLoadMethod.param(delegateClass, DELEGATE_LOAD_METHOD_PARAM_NAME);
            delayedLoadMethod.body().assign(delegateField, delegateParam);

            Set<MethodSignature> methodSignatures = new HashSet<MethodSignature>();

            //implements interfaces
            if (injectionNode.containsAspect(VirtualProxyAspect.class)) {
                for (ASTType interfaceType : proxyInterfaces) {
                    definedClass._implements(codeModel.ref(interfaceType.getName()));

                    //implement methods
                    for (ASTMethod method : interfaceType.getMethods()) {
                        //checking uniqueness
                        if (methodSignatures.add(new MethodSignature(method))) {
                            // public <type> <method_name> ( <parameters...>)
                            JType returnType = null;
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
                                                variableNamer.generateName(parameter.getASTType().getName())));
                            }

                            //define method body
                            JBlock body = methodDeclaration.body();

                            JBlock delegateNullBlock = body._if(delegateField.eq(JExpr._null()))._then();

                            //todo: add method name?
                            delegateNullBlock._throw(JExpr._new(codeModel.ref(VirtualProxyException.class)).arg(PROXY_NOT_INITIALIZED));

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
                    }
                }
            }

            return new ProxyDescriptor(definedClass);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Error while trying to build new class", e);
        }
    }

    public TypedExpression initializeProxy(InjectionBuilderContext context, TypedExpression proxyVariable, TypedExpression variableBuilder) {

        context.getBlock().add(
                proxyVariable.getExpression().invoke(DELAYED_LOAD_METHOD_NAME).arg(variableBuilder.getExpression()));

        return variableBuilder;
    }
}
