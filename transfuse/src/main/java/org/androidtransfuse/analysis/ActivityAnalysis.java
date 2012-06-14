package org.androidtransfuse.analysis;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.adapter.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilder;
import org.androidtransfuse.gen.variableBuilder.ApplicationVariableInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.ResourcesInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.processor.ManifestManager;
import org.androidtransfuse.util.TypeMirrorRunnable;
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
public class ActivityAnalysis implements Analysis<ComponentDescriptor> {

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
    private TypeMirrorUtil typeMirrorUtil;

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
                            ASTClassFactory astClassFactory, ManifestManager manifestManager, IntentFilterBuilder intentFilterBuilder, TypeMirrorUtil typeMirrorUtil) {
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
        this.typeMirrorUtil = typeMirrorUtil;
    }

    public ComponentDescriptor analyze(ASTType input) {

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

            TypeMirror type = typeMirrorUtil.getTypeMirror(new TypeMirrorRunnable<Activity>(activityAnnotation) {
                @Override
                public void run(Activity annotation) {
                    annotation.type();
                }
            });

            String activityType = type == null ? android.app.Activity.class.getName() : type.toString();

            Integer layout = layoutAnnotation == null ? null : layoutAnnotation.value();

            AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(type));

            InjectionNode layoutHandlerInjectionNode = buildLayoutHandlerInjectionNode(layoutHandlerAnnotation, context);

            activityDescriptor = new ComponentDescriptor(activityType, activityClassName);
            InjectionNode injectionNode = injectionPointFactory.buildInjectionNode(input, context);

            //application generation profile
            setupActivityProfile(activityType, activityDescriptor, injectionNode, layout, layoutHandlerInjectionNode);
        }

        //add manifest elements
        setupManifest(activityClassName.getFullyQualifiedName(), activityAnnotation, input);

        return activityDescriptor;
    }

    private InjectionNode buildLayoutHandlerInjectionNode(final LayoutHandler layoutHandlerAnnotation, AnalysisContext context) {
        if (layoutHandlerAnnotation != null) {
            TypeMirror layoutHandlerType = typeMirrorUtil.getTypeMirror(new TypeMirrorRunnable<LayoutHandler>(layoutHandlerAnnotation) {
                @Override
                public void run(LayoutHandler annotation) {
                    layoutHandlerAnnotation.value();
                }
            });

            if (layoutHandlerType != null) {
                ASTType layoutHandlerASTType = layoutHandlerType.accept(astTypeBuilderVisitorProvider.get(), null);
                return injectionPointFactory.buildInjectionNode(layoutHandlerASTType, context);
            }
        }
        return null;
    }

    private PackageClass buildPackageClass(ASTType input, String activityName) {

        PackageClass inputPackageClass = new PackageClass(input.getName());

        if (StringUtils.isBlank(activityName)) {
            return inputPackageClass.add("Activity");
        } else {
            return inputPackageClass.replaceName(activityName);
        }
    }

    private void setupManifest(String name, Activity activityAnnotation, ASTType type) {
        org.androidtransfuse.model.manifest.Activity manifestActivity = manifestActivityProvider.get();

        manifestActivity.setName(name);
        manifestActivity.setLabel(StringUtils.isBlank(activityAnnotation.label()) ? null : activityAnnotation.label());
        manifestActivity.setAllowTaskReparenting(!activityAnnotation.allowTaskReparenting()? null : true);
        manifestActivity.setAlwaysRetainTaskState(!activityAnnotation.alwaysRetainTaskState() ? null : true);
        manifestActivity.setClearTaskOnLaunch(!activityAnnotation.clearTaskOnLaunch() ? null : true);
        manifestActivity.setConfigChanges(concatenate(activityAnnotation.configChanges(), "|"));
        manifestActivity.setEnabled(activityAnnotation.enabled() ? null : false);
        manifestActivity.setExcludeFromRecents(!activityAnnotation.excludeFromRecents() ? null : true);
        manifestActivity.setExported(activityAnnotation.exported().getValue());
        manifestActivity.setFinishOnTaskLaunch(!activityAnnotation.finishOnTaskLaunch() ? null : true);
        manifestActivity.setHardwareAccelerated(!activityAnnotation.hardwareAccelerated() ? null : true);
        manifestActivity.setIcon(StringUtils.isBlank(activityAnnotation.icon()) ? null : activityAnnotation.icon());
        manifestActivity.setLaunchMode(activityAnnotation.launchMode() == LaunchMode.STANDARD ? null : activityAnnotation.launchMode());
        manifestActivity.setMultiprocess(!activityAnnotation.multiprocess() ? null : true);
        manifestActivity.setNoHistory(!activityAnnotation.noHistory() ? null : true);
        manifestActivity.setPermission(StringUtils.isBlank(activityAnnotation.permission()) ? null : activityAnnotation.permission());
        manifestActivity.setProcess(StringUtils.isBlank(activityAnnotation.process()) ? null : activityAnnotation.process());
        manifestActivity.setScreenOrientation(activityAnnotation.screenOrientation() == ScreenOrientation.UNSPECIFIED ? null : activityAnnotation.screenOrientation());
        manifestActivity.setStateNotNeeded(!activityAnnotation.stateNotNeeded() ? null : true);
        manifestActivity.setTaskAffinity(StringUtils.isBlank(activityAnnotation.taskAffinity()) ? null : activityAnnotation.taskAffinity());
        manifestActivity.setTheme(StringUtils.isBlank(activityAnnotation.theme()) ? null : activityAnnotation.theme());
        manifestActivity.setUiOptions(activityAnnotation.uiOptions() == UIOptions.NONE ? null : activityAnnotation.uiOptions());
        manifestActivity.setWindowSoftInputMode(activityAnnotation.windowSoftInputMode() == WindowSoftInputMode.STATE_UNSPECIFIED ? null : activityAnnotation.windowSoftInputMode());
        manifestActivity.setIntentFilters(intentFilterBuilder.buildIntentFilters(type));

        manifestManager.addActivity(manifestActivity);
    }

    private String concatenate(ConfigChanges[] configChanges, String separator) {
        StringBuilder builder = new StringBuilder();

        if(configChanges.length == 0){
            return null;
        }

        builder.append(configChanges[0].getLabel());
        for(int i = 1; i < configChanges.length; i++){
            builder.append(separator);
            builder.append(configChanges[i].getLabel());
        }

        return builder.toString();
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
}