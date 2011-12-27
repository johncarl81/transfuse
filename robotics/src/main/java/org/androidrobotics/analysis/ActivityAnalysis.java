package org.androidrobotics.analysis;

import android.content.Context;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Layout;
import org.androidrobotics.gen.InjectionNodeBuilderRepository;
import org.androidrobotics.gen.VariableBuilderRepositoryFactory;
import org.androidrobotics.gen.variableBuilder.ContextVariableInjectionNodeBuilder;
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
    private Provider<ContextVariableInjectionNodeBuilder> contextVariableBuilderProvider;
    private VariableBuilderRepositoryFactory variableBuilderRepositoryFactory;

    @Inject
    public ActivityAnalysis(InjectionPointFactory injectionPointFactory, Provider<ContextVariableInjectionNodeBuilder> contextVariableBuilderProvider, VariableBuilderRepositoryFactory variableBuilderRepositoryFactory) {
        this.injectionPointFactory = injectionPointFactory;
        this.contextVariableBuilderProvider = contextVariableBuilderProvider;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
    }

    public ActivityDescriptor analyzeElement(ASTType input, AnalysisRepository analysisRepository, InjectionNodeBuilderRepository injectionNodeBuilders, InterceptorRepository interceptorRepository, AOPRepository aopRepository) {

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
                    injectionPointFactory.buildInjectionPoint(input, new AnalysisContext(analysisRepository, buildVariableBuilderMap(injectionNodeBuilders), interceptorRepository, aopRepository))
            );
        }
        return activityDescriptor;
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(InjectionNodeBuilderRepository injectionNodeBuilderRepository) {

        InjectionNodeBuilderRepository subRepository = variableBuilderRepositoryFactory.buildRepository(injectionNodeBuilderRepository);

        subRepository.put(Context.class.getName(), contextVariableBuilderProvider.get());
        subRepository.put(android.app.Activity.class.getName(), contextVariableBuilderProvider.get());

        return subRepository;

    }
}
