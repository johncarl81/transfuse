package org.androidrobotics.gen;

import android.os.Bundle;
import com.sun.codemodel.*;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.ProviderDescriptor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

/**
 * @author John Ericksen
 */
@Singleton
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

        JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, descriptor.getPackageClass().getFullyQualifiedName(), ClassType.CLASS);

        definedClass._extends(android.app.Activity.class);


        JMethod onCreateMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, "onCreate");
        JVar savedInstanceState = onCreateMethod.param(Bundle.class, "savedInstanceState");

        JBlock block = onCreateMethod.body();

        //super call with saved instance state
        block.invoke(JExpr._super(), onCreateMethod).arg(savedInstanceState);

        //layout setting
        block.invoke("setContentView").arg(JExpr.lit(descriptor.getLayout()));

        //injector and injection points
        //todo: more than one?
        if (descriptor.getInjectionPoints().size() > 0) {
            FieldInjectionPoint fieldInjectionPoint = descriptor.getInjectionPoints().get(0);

            ProviderDescriptor providerDescriptor = injectorGenerator.buildInjector(fieldInjectionPoint);

            JFieldVar delegateField = definedClass.field(JMod.PRIVATE, codeModel.ref(fieldInjectionPoint.getName()), DELEGATE_NAME);
            block.assign(delegateField, providerDescriptor.getClassDefinition().staticInvoke(providerDescriptor.getInstanceMethodName()).invoke(providerDescriptor.getBuilderMethodName()));

        }


        /*for (String methodNames : descriptor.getMethods(OnCreate.class)) {
            block.invoke(delegateField, methodNames);
        }*/

    }
}
