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

    public ActivityGenerator(JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    public void generate(ActivityDescriptor descriptor, JDefinedClass injectorDefinedClass) throws IOException, JClassAlreadyExistsException {

        JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, "org.androidrobotics.example.simple." + descriptor.getName(), ClassType.CLASS);

        definedClass._extends(android.app.Activity.class);

        JFieldVar delegateField = definedClass.field(JMod.PRIVATE, codeModel.ref(descriptor.getDelegateClass()), DELEGATE_NAME);

        JMethod onCreateMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, "onCreate");
        JVar savedInstanceState = onCreateMethod.param(Bundle.class, "savedInstanceState");

        JBlock block = onCreateMethod.body();

        //super call with saved instance state
        block.invoke(JExpr._super(), onCreateMethod).arg(savedInstanceState);

        //layout setting
        block.invoke("setContentView").arg(JExpr.lit(descriptor.getLayout()));

        block.assign(delegateField, injectorDefinedClass.staticInvoke("getInstance").invoke("build_" + descriptor.getShorDelegateClassName()));

        for (String methodNames : descriptor.getMethods(OnCreate.class)) {
            block.invoke(delegateField, methodNames);
        }

    }
}
