package org.androidrobotics.gen;

import android.os.Bundle;
import com.sun.codemodel.*;
import org.androidrobotics.annotations.OnCreate;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author John Ericksen
 */
public class ActivityGenerator {

    private static final String DELEGATE_NAME = "delegate";

    private JCodeModel codeModel;
    private InjectorGenerator injectorGenerator;

    @Inject
    public ActivityGenerator(JCodeModel codeModel, InjectorGenerator injectorGenerator) {
        this.codeModel = codeModel;
        this.injectorGenerator = injectorGenerator;
    }

    public void generate(ActivityDescriptor descriptor) throws IOException, JClassAlreadyExistsException, ClassNotFoundException {

        JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, descriptor.getPackage() + "." + descriptor.getName(), ClassType.CLASS);

        definedClass._extends(android.app.Activity.class);

        JFieldVar delegateField = definedClass.field(JMod.PRIVATE, codeModel.ref(descriptor.getDelegateClass()), DELEGATE_NAME);

        JMethod onCreateMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, "onCreate");
        JVar savedInstanceState = onCreateMethod.param(Bundle.class, "savedInstanceState");

        JBlock block = onCreateMethod.body();

        //super call with saved instance state
        block.invoke(JExpr._super(), onCreateMethod).arg(savedInstanceState);

        //layout setting
        block.invoke("setContentView").arg(JExpr.lit(descriptor.getLayout()));

        FactoryDescriptor factoryDescriptor = injectorGenerator.buildInjector(descriptor);
        block.assign(delegateField, factoryDescriptor.getClassDefinition().staticInvoke(factoryDescriptor.getInstanceMethodName()).invoke(factoryDescriptor.getBuilderMethodName()));

        for (String methodNames : descriptor.getMethods(OnCreate.class)) {
            block.invoke(delegateField, methodNames);
        }

    }
}
