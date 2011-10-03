package org.androidrobotics.gen;

import android.os.Bundle;
import com.sun.codemodel.*;

import java.io.IOException;

/**
 * @author John Ericksen
 */
public class ActivityGenerator {

    private JCodeModel codeModel;

    public ActivityGenerator(JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    public void generate(ActivityDescriptor descriptor) throws IOException, JClassAlreadyExistsException {

        JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, "org.androidrobotics.example.simple." + descriptor.getName(), ClassType.CLASS);

        definedClass._extends(android.app.Activity.class);
        JMethod onCreateMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, "onCreate");
        JVar savedInstanceState = onCreateMethod.param(Bundle.class, "savedInstanceState");

        JBlock block = onCreateMethod.body();

        //super call with saved instance state
        block.invoke(JExpr._super(), onCreateMethod).arg(savedInstanceState);

        //layout setting
        block.invoke("setContentView").arg(JExpr.lit(descriptor.getLayout()));
    }
}
