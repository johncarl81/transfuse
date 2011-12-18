package org.androidrobotics.gen.proxy;

import com.sun.codemodel.*;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class DelegateConstructionGeneratorStrategy implements DelegateInstantiationGeneratorStrategy {

    private static final String DELEGATE_NAME = "delegate";

    private JCodeModel codeModel;

    @Inject
    public DelegateConstructionGeneratorStrategy(JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    @Override
    public JFieldVar addDelegateInstantiation(JDefinedClass definedClass, InjectionNode delegateNode) {
        JMethod constructor = definedClass.constructor(JMod.PUBLIC);

        JFieldVar delegateField = definedClass.field(JMod.PRIVATE, codeModel.ref(delegateNode.getClassName()), DELEGATE_NAME);

        JVar delegateParam = constructor.param(codeModel.ref(delegateNode.getClassName()), "delegateInput");
        constructor.body().assign(delegateField, delegateParam);

        return delegateField;
    }
}
