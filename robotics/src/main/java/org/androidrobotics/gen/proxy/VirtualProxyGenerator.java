package org.androidrobotics.gen.proxy;

import com.sun.codemodel.*;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTParameter;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.analysis.astAnalyzer.ProxyAspect;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.gen.UniqueVariableNamer;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.ProxyDescriptor;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class VirtualProxyGenerator {

    private static final String DELEGATE_NAME = "delegate";
    private static final String DELEGATE_LOAD_METHOD_PARAM_NAME = "delegateInput";
    protected static final String DELAYED_LOAD_METHOD_NAME = "load";
    private static final String VOID_TYPE_NAME = "void";

    private JCodeModel codeModel;
    private UniqueVariableNamer variableNamer;

    @Inject
    public VirtualProxyGenerator(JCodeModel codeModel, UniqueVariableNamer variableNamer) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
    }

    public ProxyDescriptor generateProxy(InjectionNode injectionNode) {

        JDefinedClass definedClass;
        try {

            definedClass = codeModel._class(JMod.PUBLIC, injectionNode.getClassName() + "_VProxy", ClassType.CLASS);

            //define delegate
            JClass delegateClass = codeModel.ref(injectionNode.getClassName());

            JFieldVar delegateField = definedClass.field(JMod.PRIVATE, delegateClass, DELEGATE_NAME);

            JClass providerInterface = codeModel.ref(DelayedLoad.class).narrow(delegateClass);
            definedClass._implements(providerInterface);

            JMethod delayedLoadMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, DELAYED_LOAD_METHOD_NAME);
            JVar delegateParam = delayedLoadMethod.param(delegateClass, DELEGATE_LOAD_METHOD_PARAM_NAME);
            delayedLoadMethod.body().assign(delegateField, delegateParam);

            //implements interfaces
            if (injectionNode.containsAspect(ProxyAspect.class)) {
                for (ASTType interfaceType : injectionNode.getAspect(ProxyAspect.class).getProxyInterfaces()) {
                    definedClass._implements(codeModel.ref(interfaceType.getName()));

                    //implement methods
                    for (ASTMethod method : interfaceType.getMethods()) {
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

                        //delegate invocation
                        JInvocation invocation = delegateField.invoke(method.getName());

                        for (ASTParameter parameter : method.getParameters()) {
                            invocation.arg(parameterMap.get(parameter));
                        }

                        //todo:fix void and primitive return
                        if (method.getReturnType() == null || VOID_TYPE_NAME.equals(method.getReturnType().getName())) {
                            body.add(invocation);
                        } else {
                            body._return(invocation);
                        }
                    }
                }
            }
        } catch (JClassAlreadyExistsException e) {
            throw new RoboticsAnalysisException("Error while trying to build new class", e);
        }

        return new ProxyDescriptor(definedClass);
    }

    public JExpression initalizeProxy(InjectionBuilderContext context, JExpression proxyVariable, JExpression variableBuilder) {

        context.getBlock().add(
                proxyVariable.invoke(DELAYED_LOAD_METHOD_NAME).arg(variableBuilder));

        return variableBuilder;
    }
}
