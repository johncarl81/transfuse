package org.androidrobotics.gen.proxy;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class DelegateDelayedGeneratorStrategy implements DelegateInstantiationGeneratorStrategy {

    private static final String DELEGATE_NAME = "delegate";
    protected static final String DELAYED_LOAD_METHOD_NAME = "load";

    private JCodeModel codeModel;
    private InjectionNode delegateNode;

    @Inject
    public DelegateDelayedGeneratorStrategy(JCodeModel codeModel, @Assisted InjectionNode delegateNode) {
        this.codeModel = codeModel;
        this.delegateNode = delegateNode;
    }

    @Override
    public JFieldVar addDelegateInstantiation(JDefinedClass definedClass) {

        JClass delegateClass = codeModel.ref(delegateNode.getClassName());

        JFieldVar delegateField = definedClass.field(JMod.PRIVATE, delegateClass, DELEGATE_NAME);

        JClass providerInterface = codeModel.ref(DelayedLoad.class).narrow(delegateClass);
        definedClass._implements(providerInterface);

        JMethod method = definedClass.method(JMod.PUBLIC, codeModel.VOID, DELAYED_LOAD_METHOD_NAME);
        JVar delegateParam = method.param(delegateClass, "delegateInput");
        method.body().assign(delegateField, delegateParam);

        return delegateField;
    }


    public JExpression initalizeProxy(InjectionBuilderContext context, JExpression proxyVariable, JExpression variableBuilder) {


        context.getBlock().add(
                proxyVariable.invoke(DelegateDelayedGeneratorStrategy.DELAYED_LOAD_METHOD_NAME).arg(variableBuilder));


        return variableBuilder;
    }
}
