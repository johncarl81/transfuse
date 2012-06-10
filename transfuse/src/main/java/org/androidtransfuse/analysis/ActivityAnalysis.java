package org.androidtransfuse.analysis;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.adapter.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.LayoutHandler;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilder;
import org.androidtransfuse.gen.variableBuilder.ApplicationVariableInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.ResourcesInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.processor.ManifestManager;
import org.androidtransfuse.util.TypeMirrorUtil;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;

/**
 * Activity related Analysis
 *
 * @author John Ericksen
 */
public class ActivityAnalysis {

    private InjectionPointFactory injectionPointFactory;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider;
    private Provider<ApplicationVariableInjectionNodeBuilder> applicationVariableBuilderProvider;
    private Provider<org.androidtransfuse.model.manifest.Activity> manifestActivityProvider;
    private InjectionNodeBuilderRepository injectionNodeBuilders;
    private ActivityComponentBuilderRepository activityComponentBuilderRepository;
    private AnalysisContextFactory analysisContextFactory;
    private Provider<ASTTypeBuilderVisitor> astTypeBuilderVisitorProvider;
    private ASTClassFactory astClassFactory;
    private ManifestManager manifestManager;
    private IntentFilterBuilder intentFilterBuilder;

    @Inject
    public ActivityAnalysis(InjectionPointFactory injectionPointFactory,
                            VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                            InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory,
                            Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider,
                            Provider<ApplicationVariableInjectionNodeBuilder> applicationVariableBuilderProvider,
                            Provider<org.androidtransfuse.model.manifest.Activity> manifestActivityProvider,
                            InjectionNodeBuilderRepository injectionNodeBuilders,
                            ActivityComponentBuilderRepository activityComponentBuilderRepository,
                            AnalysisContextFactory analysisContextFactory,
                            Provider<ASTTypeBuilderVisitor> astTypeBuilderVisitorProvider,
                            ASTClassFactory astClassFactory, ManifestManager manifestManager, IntentFilterBuilder intentFilterBuilder) {
        this.injectionPointFactory = injectionPointFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.resourcesInjectionNodeBuilderProvider = resourcesInjectionNodeBuilderProvider;
        this.applicationVariableBuilderProvider = applicationVariableBuilderProvider;
        this.manifestActivityProvider = manifestActivityProvider;
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.activityComponentBuilderRepository = activityComponentBuilderRepository;
        this.analysisContextFactory = analysisContextFactory;
        this.astTypeBuilderVisitorProvider = astTypeBuilderVisitorProvider;
        this.astClassFactory = astClassFactory;
        this.manifestManager = manifestManager;
        this.intentFilterBuilder = intentFilterBuilder;
    }

    public ComponentDescriptor analyzeElement(ASTType input) {

        Activity activityAnnotation = input.getAnnotation(Activity.class);
        PackageClass activityClassName;
        ComponentDescriptor activityDescriptor = null;

        if (input.extendsFrom(astClassFactory.buildASTClassType(android.app.Activity.class))) {
            //vanilla Android activity
            PackageClass activityPackageClass = new PackageClass(input.getName());
            activityClassName = buildPackageClass(input, activityPackageClass.getClassName());
        } else {
            //generated Android activity
            activityClassName = buildPackageClass(input, activityAnnotation.name());

            Layout layoutAnnotation = input.getAnnotation(Layout.class);
            LayoutHandler layoutHandlerAnnotation = input.getAnnotation(LayoutHandler.class);

            TypeMirror type = TypeMirrorUtil.getInstance().getTypeMirror(new ActivityTypeRunnable(activityAnnotation));

            String activityType = buildActivityType(type);

            Integer layout = buildLayout(layoutAnnotation);

            AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(type));

            InjectionNode layoutHandlerInjectionNode = buildLayoutHandlerInjectionNode(layoutHandlerAnnotation, context);

            activityDescriptor = new ComponentDescriptor(activityType, activityClassName);
            InjectionNode injectionNode = injectionPointFactory.buildInjectionNode(input, context);

            //application generation profile
            setupActivityProfile(activityType, activityDescriptor, injectionNode, layout, layoutHandlerInjectionNode);
        }

        //add manifest elements
        setupManifest(activityClassName.getFullyQualifiedName(), activityAnnotation.label(), input);

        return activityDescriptor;
    }

    private Integer buildLayout(Layout layoutAnnotation) {
        if (layoutAnnotation != null) {
            return layoutAnnotation.value();
        }
        return null;
    }

    private InjectionNode buildLayoutHandlerInjectionNode(final LayoutHandler layoutHandlerAnnotation, AnalysisContext context) {
        if (layoutHandlerAnnotation != null) {
            TypeMirror layoutHandlerType = TypeMirrorUtil.getInstance().getTypeMirror(new LayoutHandlerTypeRunnable(layoutHandlerAnnotation));

            if (layoutHandlerType != null) {
                ASTType layoutHandlerASTType = layoutHandlerType.accept(astTypeBuilderVisitorProvider.get(), null);
                return injectionPointFactory.buildInjectionNode(layoutHandlerASTType, context);
            }
        }
        return null;
    }

    private String buildActivityType(TypeMirror type) {
        if (type != null) {
            return type.toString();
        } else {
            return android.app.Activity.class.getName();
        }
    }

    private PackageClass buildPackageClass(ASTType input, String activityName) {

        PackageClass inputPackageClass = new PackageClass(input.getName());

        if (StringUtils.isBlank(activityName)) {
            return inputPackageClass.add("Activity");
        } else {
            return inputPackageClass.replaceName(activityName);
        }
    }

    private void setupManifest(String name, String label, ASTType type) {
        org.androidtransfuse.model.manifest.Activity manifestActivity = manifestActivityProvider.get();

        manifestActivity.setName(name);
        manifestActivity.setLabel(StringUtils.isBlank(label) ? null : label);
        manifestActivity.setIntentFilters(intentFilterBuilder.buildIntentFilters(type));

        manifestManager.addActivity(manifestActivity);
    }

    private void setupActivityProfile(String activityType, ComponentDescriptor activityDescriptor, InjectionNode injectionNode, Integer layout, InjectionNode layoutHandlerInjectionNode) {
        ComponentBuilder activityComponentBuilder = activityComponentBuilderRepository.buildComponentBuilder(activityType, injectionNode, layout, layoutHandlerInjectionNode);

        activityDescriptor.getComponentBuilders().add(activityComponentBuilder);
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(TypeMirror activityType) {

        InjectionNodeBuilderRepository subRepository = variableBuilderRepositoryFactory.buildRepository(injectionNodeBuilders);

        subRepository.put(Context.class.getName(), variableInjectionBuilderFactory.buildContextVariableInjectionNodeBuilder(Context.class));
        subRepository.put(Application.class.getName(), applicationVariableBuilderProvider.get());
        subRepository.put(android.app.Activity.class.getName(), variableInjectionBuilderFactory.buildContextVariableInjectionNodeBuilder(android.app.Activity.class));
        subRepository.put(Resources.class.getName(), resourcesInjectionNodeBuilderProvider.get());

        //todo: map inheritance of activity type?
        if (activityType != null) {
            subRepository.put(activityType.toString(), variableInjectionBuilderFactory.buildContextVariableInjectionNodeBuilder(android.app.Activity.class));
        }

        return subRepository;

    }

    private static final class ActivityTypeRunnable implements Runnable {

        private Activity activityAnnotation;

        private ActivityTypeRunnable(Activity activityAnnotation) {
            this.activityAnnotation = activityAnnotation;
        }

        public void run() {
            //accessing this throws an exception, caught in TypeMiirrorUtil
            activityAnnotation.type();
        }
    }

    private static final class LayoutHandlerTypeRunnable implements Runnable {

        private LayoutHandler layoutHandler;

        private LayoutHandlerTypeRunnable(LayoutHandler layoutHandler) {
            this.layoutHandler = layoutHandler;
        }

        public void run() {
            //accessing this throws an exception, caught in TypeMiirrorUtil
            layoutHandler.value();
        }
    }
}