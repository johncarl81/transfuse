package org.androidrobotics.gen;

import android.os.Bundle;
import com.sun.codemodel.*;
import org.androidrobotics.annotations.OnCreate;

import java.io.IOException;

/**
 * @author John Ericksen
 */
public class ActivityGenerator {

    private static final String DELEGATE_NAME = "delegate";

    private JCodeModel codeModel;
    private InjectionGenerator injectionGenerator;

    public ActivityGenerator(JCodeModel codeModel, InjectionGenerator injectionGenerator) {
        this.codeModel = codeModel;
        this.injectionGenerator = injectionGenerator;
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

        JDefinedClass injectorDefinedClass = injectionGenerator.buildInjector(descriptor);

        block.assign(delegateField, injectorDefinedClass.staticInvoke("getInstance").invoke("build_" + descriptor.getShorDelegateClassName()));

        for (String methodNames : descriptor.getMethods(OnCreate.class)) {
            block.invoke(delegateField, methodNames);
        }

    }
}
