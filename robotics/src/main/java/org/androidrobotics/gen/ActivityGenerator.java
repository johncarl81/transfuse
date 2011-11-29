package org.androidrobotics.gen;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import com.sun.codemodel.*;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.provider.VibratorProvider;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.IOException;

/**
 * @author John Ericksen
 */
@Singleton
public class ActivityGenerator {

    private JCodeModel codeModel;
    private InjectionFragmentGenerator injectionFragmentGenerator;
    private Provider<ContextVariableBuilder> contextVariableBuilderProvider;
    private ProviderVariableBuilderFactory providerVariableBuilderFactory;
    private Provider<VariableBuilderRepository> variableBuilderRepositoryProvider;

    @Inject
    public ActivityGenerator(JCodeModel codeModel, InjectionFragmentGenerator injectionFragmentGenerator, Provider<ContextVariableBuilder> contextVariableBuilderProvider, ProviderVariableBuilderFactory providerVariableBuilderFactory, Provider<VariableBuilderRepository> variableBuilderRepositoryProvider) {
        this.codeModel = codeModel;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.contextVariableBuilderProvider = contextVariableBuilderProvider;
        this.providerVariableBuilderFactory = providerVariableBuilderFactory;
        this.variableBuilderRepositoryProvider = variableBuilderRepositoryProvider;
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

            VariableBuilderRepository variableBuilderMap = buildVariableBuilderMap();

            injectionFragmentGenerator.buildFragment(block, definedClass, fieldInjectionPoint.getInjectionNode(), variableBuilderMap);
        }
    }

    private VariableBuilderRepository buildVariableBuilderMap() {

        VariableBuilderRepository repository = variableBuilderRepositoryProvider.get();

        repository.put(Context.class.getName(), contextVariableBuilderProvider.get());
        repository.put(Activity.class.getName(), contextVariableBuilderProvider.get());
        repository.put(Vibrator.class.getName(), providerVariableBuilderFactory.buildProviderVariableBuilder(VibratorProvider.class));

        return repository;

    }
}
