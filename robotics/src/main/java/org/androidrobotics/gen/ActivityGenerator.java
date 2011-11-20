package org.androidrobotics.gen;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.sun.codemodel.*;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.model.FieldInjectionPoint;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class ActivityGenerator {

    private static final String DELEGATE_NAME = "delegate";

    private JCodeModel codeModel;
    private InjectionFragmentGenerator injectionFragmentGenerator;

    @Inject
    public ActivityGenerator(JCodeModel codeModel, InjectionFragmentGenerator injectionFragmentGenerator) {
        this.codeModel = codeModel;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
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

            Map<String, VariableBuilder> variableBuilderMap = buildVariableBuilderMap();

            injectionFragmentGenerator.buildFragment(block, definedClass, fieldInjectionPoint.getInjectionNode(), variableBuilderMap);
        }
    }

    private Map<String, VariableBuilder> buildVariableBuilderMap() {
        Map<String, VariableBuilder> builderMap = new HashMap<String, VariableBuilder>();

        builderMap.put(Context.class.getName(), new ContextVariableBuilder());
        builderMap.put(Activity.class.getName(), new ContextVariableBuilder());

        return builderMap;

    }
}
