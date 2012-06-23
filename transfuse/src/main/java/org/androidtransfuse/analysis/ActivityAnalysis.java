package org.androidtransfuse.analysis;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.adapter.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.astAnalyzer.ProviderInjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.ActivityComponentBuilderRepository;
import org.androidtransfuse.analysis.repository.BindingRepositoryFactory;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.LayoutBuilder;
import org.androidtransfuse.gen.componentBuilder.NoOpLayoutBuilder;
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
    private InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory;
    private InjectionNodeBuilderRepository injectionNodeBuilderRepository;
    private Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider;
    private Provider<ApplicationVariableInjectionNodeBuilder> applicationVariableBuilderProvider;
    private Provider<org.androidtransfuse.model.manifest.Activity> manifestActivityProvider;
    private ActivityComponentBuilderRepository activityComponentBuilderRepository;
    private AnalysisContextFactory analysisContextFactory;
    private Provider<ASTTypeBuilderVisitor> astTypeBuilderVisitorProvider;
    private ASTClassFactory astClassFactory;
    private ManifestManager manifestManager;
    private IntentFilterBuilder intentFilterBuilder;
    private TypeMirrorUtil typeMirrorUtil;
    private ComponentBuilderFactory componentBuilderFactory;
    private MetaDataBuilder metadataBuilder;
    private BindingRepositoryFactory bindingRepositoryFactory;
    private ASTTypeBuilderVisitor astTypeBuilderVisitor;

    @Inject
    public ActivityAnalysis(InjectionPointFactory injectionPointFactory,
                            VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                            InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory,
                            InjectionNodeBuilderRepository injectionNodeBuilderRepository,
                            Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider,
                            Provider<ApplicationVariableInjectionNodeBuilder> applicationVariableBuilderProvider,
                            Provider<org.androidtransfuse.model.manifest.Activity> manifestActivityProvider,
                            ActivityComponentBuilderRepository activityComponentBuilderRepository,
                            AnalysisContextFactory analysisContextFactory,
                            Provider<ASTTypeBuilderVisitor> astTypeBuilderVisitorProvider,
                            ASTClassFactory astClassFactory,
                            ManifestManager manifestManager,
                            IntentFilterBuilder intentFilterBuilder,
                            TypeMirrorUtil typeMirrorUtil,
                            ComponentBuilderFactory componentBuilderFactory, MetaDataBuilder metadataBuilder, BindingRepositoryFactory bindingRepositoryFactory, ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        this.injectionPointFactory = injectionPointFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
        this.injectionNodeBuilderRepository = injectionNodeBuilderRepository;
        this.resourcesInjectionNodeBuilderProvider = resourcesInjectionNodeBuilderProvider;
        this.applicationVariableBuilderProvider = applicationVariableBuilderProvider;
        this.manifestActivityProvider = manifestActivityProvider;
        this.activityComponentBuilderRepository = activityComponentBuilderRepository;
        this.analysisContextFactory = analysisContextFactory;
        this.astTypeBuilderVisitorProvider = astTypeBuilderVisitorProvider;
        this.astClassFactory = astClassFactory;
        this.manifestManager = manifestManager;
        this.intentFilterBuilder = intentFilterBuilder;
        this.typeMirrorUtil = typeMirrorUtil;
        this.componentBuilderFactory = componentBuilderFactory;
        this.metadataBuilder = metadataBuilder;
        this.bindingRepositoryFactory = bindingRepositoryFactory;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
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

            TypeMirror type = typeMirrorUtil.getTypeMirror(new ActivityTypeMirrorRunnable(activityAnnotation));

            String activityType = type == null ? android.app.Activity.class.getName() : type.toString();

            Integer layout = layoutAnnotation == null ? null : layoutAnnotation.value();

            AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(type));

            InjectionNode layoutHandlerInjectionNode = buildLayoutHandlerInjectionNode(layoutHandlerAnnotation, context);

            activityDescriptor = new ComponentDescriptor(activityType, activityClassName);

            //application generation profile
            setupActivityProfile(activityType, activityDescriptor, input, context, layout, layoutHandlerInjectionNode);
        }

        //add manifest elements
        setupManifest(activityClassName.getFullyQualifiedName(), activityAnnotation, input);

        return activityDescriptor;
    }

    private InjectionNode buildLayoutHandlerInjectionNode(final LayoutHandler layoutHandlerAnnotation, AnalysisContext context) {
        if (layoutHandlerAnnotation != null) {
            TypeMirror layoutHandlerType = typeMirrorUtil.getTypeMirror(new LayoutHandlerTypeMirrorRunnable(layoutHandlerAnnotation));

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
        manifestActivity.setLabel(checkBlank(activityAnnotation.label()));
        manifestActivity.setAllowTaskReparenting(checkDefault(activityAnnotation.allowTaskReparenting(), false));
        manifestActivity.setAlwaysRetainTaskState(checkDefault(activityAnnotation.alwaysRetainTaskState(), false));
        manifestActivity.setClearTaskOnLaunch(checkDefault(activityAnnotation.clearTaskOnLaunch(), false));
        manifestActivity.setConfigChanges(concatenate(activityAnnotation.configChanges(), "|"));
        manifestActivity.setEnabled(checkDefault(activityAnnotation.enabled(), true));
        manifestActivity.setExcludeFromRecents(checkDefault(activityAnnotation.excludeFromRecents(), false));
        manifestActivity.setExported(activityAnnotation.exported().getValue());
        manifestActivity.setFinishOnTaskLaunch(checkDefault(activityAnnotation.finishOnTaskLaunch(), false));
        manifestActivity.setHardwareAccelerated(checkDefault(activityAnnotation.hardwareAccelerated(), false));
        manifestActivity.setIcon(checkBlank(activityAnnotation.icon()));
        manifestActivity.setLaunchMode(checkDefault(activityAnnotation.launchMode(), LaunchMode.STANDARD));
        manifestActivity.setMultiprocess(checkDefault(activityAnnotation.multiprocess(), false));
        manifestActivity.setNoHistory(checkDefault(activityAnnotation.noHistory(), false));
        manifestActivity.setPermission(checkBlank(activityAnnotation.permission()));
        manifestActivity.setProcess(checkBlank(activityAnnotation.process()));
        manifestActivity.setScreenOrientation(checkDefault(activityAnnotation.screenOrientation(), ScreenOrientation.UNSPECIFIED));
        manifestActivity.setStateNotNeeded(checkDefault(activityAnnotation.stateNotNeeded(), false));
        manifestActivity.setTaskAffinity(checkBlank(activityAnnotation.taskAffinity()));
        manifestActivity.setTheme(checkBlank(activityAnnotation.theme()));
        manifestActivity.setUiOptions(checkDefault(activityAnnotation.uiOptions(), UIOptions.NONE));
        manifestActivity.setWindowSoftInputMode(checkDefault(activityAnnotation.windowSoftInputMode(), WindowSoftInputMode.STATE_UNSPECIFIED));
        manifestActivity.setIntentFilters(intentFilterBuilder.buildIntentFilters(type));
        manifestActivity.setMetaData(metadataBuilder.buildMetaData(type));

        manifestManager.addActivity(manifestActivity);
    }

    private <T> T checkDefault(T input, T defaultValue) {
        if (input.equals(defaultValue)) {
            return null;
        }
        return input;
    }

    private String checkBlank(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        return input;
    }

    private String concatenate(ConfigChanges[] configChanges, String separator) {
        StringBuilder builder = new StringBuilder();

        if (configChanges.length == 0) {
            return null;
        }

        builder.append(configChanges[0].getLabel());
        for (int i = 1; i < configChanges.length; i++) {
            builder.append(separator);
            builder.append(configChanges[i].getLabel());
        }

        return builder.toString();
    }

    private void setupActivityProfile(String activityType, ComponentDescriptor activityDescriptor, ASTType astType, AnalysisContext context, Integer layout, InjectionNode layoutHandlerInjectionNode) {

        try {
            LayoutBuilder layoutBuilder;
            if (layout == null) {
                if (layoutHandlerInjectionNode == null) {
                    layoutBuilder = new NoOpLayoutBuilder();
                } else {
                    layoutBuilder = componentBuilderFactory.buildLayoutHandlerBuilder(layoutHandlerInjectionNode);
                }
            } else {
                layoutBuilder = componentBuilderFactory.buildRLayoutBuilder(layout);
            }

            ASTMethod onCreateASTMethod = astClassFactory.buildASTClassMethod(android.app.Activity.class.getDeclaredMethod("onCreate", Bundle.class));
            activityDescriptor.setMethodBuilder(componentBuilderFactory.buildOnCreateMethodBuilder(onCreateASTMethod, layoutBuilder));

            activityDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildInjectionNodeFactory(astType, context));

            activityDescriptor.addGenerators(activityComponentBuilderRepository.getGenerators(activityType));

        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while trying to find onCreate Method", e);
        }

    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(TypeMirror activityType) {

        injectionNodeBuilderRepository.putType(Context.class, variableInjectionBuilderFactory.buildContextVariableInjectionNodeBuilder(Context.class));
        injectionNodeBuilderRepository.putType(Application.class, applicationVariableBuilderProvider.get());
        injectionNodeBuilderRepository.putType(android.app.Activity.class, variableInjectionBuilderFactory.buildContextVariableInjectionNodeBuilder(android.app.Activity.class));
        injectionNodeBuilderRepository.putType(Resources.class, resourcesInjectionNodeBuilderProvider.get());

        //todo: map inheritance of activity type?
        if (activityType != null) {
            ASTType activityASTType = activityType.accept(astTypeBuilderVisitor, null);
            injectionNodeBuilderRepository.putType(activityASTType, variableInjectionBuilderFactory.buildContextVariableInjectionNodeBuilder(android.app.Activity.class));
        }

        bindingRepositoryFactory.addBindingAnnotations(injectionNodeBuilderRepository);

        injectionNodeBuilderRepositoryFactory.addApplicationInjections(injectionNodeBuilderRepository);

        return injectionNodeBuilderRepository;

    }

    private static class ActivityTypeMirrorRunnable extends TypeMirrorRunnable<Activity> {
        public ActivityTypeMirrorRunnable(Activity activityAnnotation) {
            super(activityAnnotation);
        }

        @Override
        public void run(Activity annotation) {
            annotation.type();
        }
    }

    private static class LayoutHandlerTypeMirrorRunnable extends TypeMirrorRunnable<LayoutHandler> {
        public LayoutHandlerTypeMirrorRunnable(LayoutHandler layoutHandlerAnnotation) {
            super(layoutHandlerAnnotation);
        }

        @Override
        public void run(LayoutHandler annotation) {
            annotation.value();
        }
    }
}