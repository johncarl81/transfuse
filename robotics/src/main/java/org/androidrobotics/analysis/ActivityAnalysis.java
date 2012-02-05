package org.androidrobotics.analysis;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Intent;
import org.androidrobotics.annotations.IntentFilters;
import org.androidrobotics.annotations.Layout;
import org.androidrobotics.gen.InjectionNodeBuilderRepository;
import org.androidrobotics.gen.VariableBuilderRepositoryFactory;
import org.androidrobotics.gen.variableBuilder.ApplicationVariableInjectionNodeBuilder;
import org.androidrobotics.gen.variableBuilder.ContextVariableInjectionNodeBuilder;
import org.androidrobotics.gen.variableBuilder.ResourcesInjectionNodeBuilder;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.model.PackageClass;
import org.androidrobotics.model.manifest.Action;
import org.androidrobotics.model.manifest.Category;
import org.androidrobotics.model.manifest.IntentFilter;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity related Analysis
 *
 * @author John Ericksen
 */
public class ActivityAnalysis {

    private InjectionPointFactory injectionPointFactory;
    private Provider<ContextVariableInjectionNodeBuilder> contextVariableBuilderProvider;
    private VariableBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider;
    private Provider<ApplicationVariableInjectionNodeBuilder> applicationVariableBuilderProvider;

    @Inject
    public ActivityAnalysis(InjectionPointFactory injectionPointFactory,
                            Provider<ContextVariableInjectionNodeBuilder> contextVariableBuilderProvider,
                            VariableBuilderRepositoryFactory variableBuilderRepositoryFactory,
                            Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider,
                            Provider<ApplicationVariableInjectionNodeBuilder> applicationVariableBuilderProvider) {
        this.injectionPointFactory = injectionPointFactory;
        this.contextVariableBuilderProvider = contextVariableBuilderProvider;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.resourcesInjectionNodeBuilderProvider = resourcesInjectionNodeBuilderProvider;
        this.applicationVariableBuilderProvider = applicationVariableBuilderProvider;
    }

    public ActivityDescriptor analyzeElement(ASTType input, AnalysisRepository analysisRepository, InjectionNodeBuilderRepository injectionNodeBuilders, AOPRepository aopRepository) {

        Activity activityAnnotation = input.getAnnotation(Activity.class);
        Layout layoutAnnotation = input.getAnnotation(Layout.class);
        IntentFilters intentFilters = input.getAnnotation(IntentFilters.class);

        ActivityDescriptor activityDescriptor = null;

        if (activityAnnotation != null) {
            activityDescriptor = new ActivityDescriptor();

            String name = input.getName();
            String packageName = name.substring(0, name.lastIndexOf('.'));

            activityDescriptor.setPackageClass(new PackageClass(packageName, activityAnnotation.name()));
            activityDescriptor.setLabel(activityAnnotation.label());
            activityDescriptor.setLayout(layoutAnnotation.value());

            AnalysisContext context = new AnalysisContext(analysisRepository, buildVariableBuilderMap(injectionNodeBuilders), aopRepository);

            activityDescriptor.addInjectionNode(
                    injectionPointFactory.buildInjectionNode(input, context));

            org.androidrobotics.model.manifest.Activity manifestActivity = new org.androidrobotics.model.manifest.Activity("." + activityAnnotation.name(), activityAnnotation.label());
            manifestActivity.setMergeTag("yes");//todo: common tagger?
            manifestActivity.setIntentFilters(buildIntentFilters(intentFilters));

            activityDescriptor.setManifestActivity(manifestActivity);
        }
        return activityDescriptor;
    }

    private List<IntentFilter> buildIntentFilters(IntentFilters intentFilters) {
        List<IntentFilter> convertedIntentFilters = new ArrayList<IntentFilter>();

        if (intentFilters != null) {

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.setMergeTag("yes"); //todo: common tagger?
            convertedIntentFilters.add(intentFilter);

            for (Intent intentAnnotation : intentFilters.value()) {
                switch (intentAnnotation.type()) {
                    case ACTION:
                        Action action = new Action();
                        action.setMergeTag("yes");
                        action.setName(intentAnnotation.name());
                        intentFilter.getActions().add(action);
                        break;
                    case CATEGORY:
                        Category category = new Category();
                        category.setMergeTag("yes");
                        category.setName(intentAnnotation.name());
                        intentFilter.getCategories().add(category);
                        break;
                }
            }
        }

        return convertedIntentFilters;
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(InjectionNodeBuilderRepository injectionNodeBuilderRepository) {

        InjectionNodeBuilderRepository subRepository = variableBuilderRepositoryFactory.buildRepository(injectionNodeBuilderRepository);

        subRepository.put(Context.class.getName(), contextVariableBuilderProvider.get());
        subRepository.put(Application.class.getName(), applicationVariableBuilderProvider.get());
        subRepository.put(android.app.Activity.class.getName(), contextVariableBuilderProvider.get());
        subRepository.put(Resources.class.getName(), resourcesInjectionNodeBuilderProvider.get());

        return subRepository;

    }
}
