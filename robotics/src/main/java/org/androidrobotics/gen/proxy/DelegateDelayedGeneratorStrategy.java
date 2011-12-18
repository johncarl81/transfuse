package org.androidrobotics.gen.proxy;

import com.sun.codemodel.*;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class DelegateDelayedGeneratorStrategy implements DelegateInstantiationGeneratorStrategy {

    private static final String DELEGATE_NAME = "delegate";
    private static final String DELEGATE_LOAD_METHOD_PARAM_NAME = "delegateInput";
    protected static final String DELAYED_LOAD_METHOD_NAME = "load";

    private JCodeModel codeModel;

    @Inject
    public DelegateDelayedGeneratorStrategy(JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    @Override
    public JFieldVar addDelegateInstantiation(JDefinedClass definedClass, InjectionNode delegateNode) {

        JClass delegateClass = codeModel.ref(delegateNode.getClassName());

        JFieldVar delegateField = definedClass.field(JMod.PRIVATE, delegateClass, DELEGATE_NAME);

        JClass providerInterface = codeModel.ref(DelayedLoad.class).narrow(delegateClass);
        definedClass._implements(providerInterface);

        JMethod method = definedClass.method(JMod.PUBLIC, codeModel.VOID, DELAYED_LOAD_METHOD_NAME);
        JVar delegateParam = method.param(delegateClass, DELEGATE_LOAD_METHOD_PARAM_NAME);
        method.body().assign(delegateField, delegateParam);

        return delegateField;
    }

    public JExpression initalizeProxy(InjectionBuilderContext context, JExpression proxyVariable, JExpression variableBuilder) {

        context.getBlock().add(
                proxyVariable.invoke(DelegateDelayedGeneratorStrategy.DELAYED_LOAD_METHOD_NAME).arg(variableBuilder));

        return variableBuilder;
    }
}
