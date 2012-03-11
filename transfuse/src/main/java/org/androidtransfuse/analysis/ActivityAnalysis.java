package org.androidtransfuse.analysis;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Intent;
import org.androidtransfuse.annotations.IntentFilters;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.gen.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.variableBuilder.ApplicationVariableInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.ContextVariableInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.ResourcesInjectionNodeBuilder;
import org.androidtransfuse.model.ActivityDescriptor;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.model.manifest.Action;
import org.androidtransfuse.model.manifest.Category;
import org.androidtransfuse.model.manifest.IntentFilter;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
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
    private InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider;
    private Provider<ApplicationVariableInjectionNodeBuilder> applicationVariableBuilderProvider;
    private Provider<org.androidtransfuse.model.manifest.Activity> manifestActivityProvider;
    private Provider<IntentFilter> intentFilterProvider;
    private Provider<Action> actionProvider;
    private Provider<Category> categoryProvider;
    private InjectionNodeBuilderRepository injectionNodeBuilders;
    private AOPRepository aopRepository;

    @Inject
    public ActivityAnalysis(InjectionPointFactory injectionPointFactory,
                            Provider<ContextVariableInjectionNodeBuilder> contextVariableBuilderProvider,
                            InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory,
                            Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider,
                            Provider<ApplicationVariableInjectionNodeBuilder> applicationVariableBuilderProvider,
                            Provider<org.androidtransfuse.model.manifest.Activity> manifestActivityProvider,
                            Provider<Category> categoryProvider,
                            Provider<Action> actionProvider,
                            Provider<IntentFilter> intentFilterProvider,
                            AOPRepository aopRepository,
                            InjectionNodeBuilderRepository injectionNodeBuilders) {
        this.injectionPointFactory = injectionPointFactory;
        this.contextVariableBuilderProvider = contextVariableBuilderProvider;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.resourcesInjectionNodeBuilderProvider = resourcesInjectionNodeBuilderProvider;
        this.applicationVariableBuilderProvider = applicationVariableBuilderProvider;
        this.manifestActivityProvider = manifestActivityProvider;
        this.categoryProvider = categoryProvider;
        this.actionProvider = actionProvider;
        this.intentFilterProvider = intentFilterProvider;
        this.aopRepository = aopRepository;
        this.injectionNodeBuilders = injectionNodeBuilders;
    }

    public ActivityDescriptor analyzeElement(ASTType input, AnalysisRepository analysisRepository) {

        Activity activityAnnotation = input.getAnnotation(Activity.class);
        Layout layoutAnnotation = input.getAnnotation(Layout.class);
        IntentFilters intentFilters = input.getAnnotation(IntentFilters.class);

        ActivityDescriptor activityDescriptor = new ActivityDescriptor();

        //http://blog.retep.org/2009/02/13/getting-class-values-from-annotations-in-an-annotationprocessor/
        TypeMirror type = null;
        try {
            activityAnnotation.type();
        } catch (MirroredTypeException mte) {
            type = mte.getTypeMirror();
        }

        if (type != null) {
            activityDescriptor.setType(type.toString());
        }

        String name = input.getName();
        String packageName = name.substring(0, name.lastIndexOf('.'));
        String deleagateName = name.substring(name.lastIndexOf('.') + 1);

        if (StringUtils.isBlank(activityAnnotation.name())) {
            activityDescriptor.setPackageClass(new PackageClass(packageName, deleagateName + "Activity"));
        } else {
            activityDescriptor.setPackageClass(new PackageClass(packageName, activityAnnotation.name()));
        }

        activityDescriptor.setLabel(activityAnnotation.label());
        if (layoutAnnotation != null) {
            activityDescriptor.setLayout(layoutAnnotation.value());
        }

        AnalysisContext context = new AnalysisContext(analysisRepository, buildVariableBuilderMap(type), aopRepository);

        activityDescriptor.addInjectionNode(
                injectionPointFactory.buildInjectionNode(input, context));

        org.androidtransfuse.model.manifest.Activity manifestActivity = manifestActivityProvider.get();


        manifestActivity.setName(activityDescriptor.getPackageClass().getFullyQualifiedName());
        manifestActivity.setLabel(activityAnnotation.label());
        manifestActivity.setIntentFilters(buildIntentFilters(intentFilters));

        activityDescriptor.setManifestActivity(manifestActivity);

        return activityDescriptor;
    }

    private List<IntentFilter> buildIntentFilters(IntentFilters intentFilters) {
        List<IntentFilter> convertedIntentFilters = new ArrayList<IntentFilter>();

        if (intentFilters != null) {

            IntentFilter intentFilter = intentFilterProvider.get();
            convertedIntentFilters.add(intentFilter);

            for (Intent intentAnnotation : intentFilters.value()) {
                switch (intentAnnotation.type()) {
                    case ACTION:
                        Action action = actionProvider.get();
                        action.setName(intentAnnotation.name());
                        intentFilter.getActions().add(action);
                        break;
                    case CATEGORY:
                        Category category = categoryProvider.get();
                        category.setName(intentAnnotation.name());
                        intentFilter.getCategories().add(category);
                        break;
                    default:
                        //noop
                        break;
                }
            }
        }

        return convertedIntentFilters;
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(TypeMirror activityType) {

        InjectionNodeBuilderRepository subRepository = variableBuilderRepositoryFactory.buildRepository(injectionNodeBuilders);

        subRepository.put(Context.class.getName(), contextVariableBuilderProvider.get());
        subRepository.put(Application.class.getName(), applicationVariableBuilderProvider.get());
        subRepository.put(android.app.Activity.class.getName(), contextVariableBuilderProvider.get());
        subRepository.put(Resources.class.getName(), resourcesInjectionNodeBuilderProvider.get());

        //todo: map inheritance of activity type?
        if (activityType != null) {
            subRepository.put(activityType.toString(), contextVariableBuilderProvider.get());
        }

        return subRepository;

    }
}
