package org.androidrobotics.analysis;

import android.content.Context;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Layout;
import org.androidrobotics.gen.VariableBuilderRepository;
import org.androidrobotics.gen.VariableBuilderRepositoryFactory;
import org.androidrobotics.gen.variableBuilder.ContextVariableBuilder;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.model.PackageClass;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Activity related Analysis
 *
 * @author John Ericksen
 */
public class ActivityAnalysis {

    private InjectionPointFactory injectionPointFactory;
    private Provider<ContextVariableBuilder> contextVariableBuilderProvider;
    private VariableBuilderRepositoryFactory variableBuilderRepositoryFactory;

    @Inject
    public ActivityAnalysis(InjectionPointFactory injectionPointFactory, Provider<ContextVariableBuilder> contextVariableBuilderProvider, VariableBuilderRepositoryFactory variableBuilderRepositoryFactory) {
        this.injectionPointFactory = injectionPointFactory;
        this.contextVariableBuilderProvider = contextVariableBuilderProvider;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
    }

    public ActivityDescriptor analyzeElement(ASTType input, AnalysisRepository analysisRepository, VariableBuilderRepository variableBuilders) {

        Activity activityAnnotation = input.getAnnotation(Activity.class);
        Layout layoutAnnotation = input.getAnnotation(Layout.class);

        ActivityDescriptor activityDescriptor = null;

        if (activityAnnotation != null) {
            activityDescriptor = new ActivityDescriptor();

            String name = input.getName();
            String packageName = name.substring(0, name.lastIndexOf('.'));

            activityDescriptor.setPackageClass(new PackageClass(packageName, activityAnnotation.value()));
            activityDescriptor.setLayout(layoutAnnotation.value());

            activityDescriptor.addInjectionPoint(
                    injectionPointFactory.buildInjectionPoint(input, analysisRepository, buildVariableBuilderMap(variableBuilders))
            );
        }
        return activityDescriptor;
    }

    private VariableBuilderRepository buildVariableBuilderMap(VariableBuilderRepository variableBuilderRepository) {

        VariableBuilderRepository subRepository = variableBuilderRepositoryFactory.buildRepository(variableBuilderRepository);

        subRepository.put(Context.class.getName(), contextVariableBuilderProvider.get());
        subRepository.put(android.app.Activity.class.getName(), contextVariableBuilderProvider.get());

        return subRepository;

    }
}
