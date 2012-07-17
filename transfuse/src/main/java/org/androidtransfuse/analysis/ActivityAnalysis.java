package org.androidtransfuse.analysis;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.adapter.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.ActivityComponentBuilderRepositoryFactory;
import org.androidtransfuse.analysis.repository.BindingRepositoryFactory;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.ContextScopeComponentBuilder;
import org.androidtransfuse.gen.componentBuilder.LayoutBuilder;
import org.androidtransfuse.gen.componentBuilder.NoOpLayoutBuilder;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.processor.ManifestManager;
import org.androidtransfuse.scope.ContextScopeHolder;
import org.androidtransfuse.util.TypeMirrorRunnable;
import org.androidtransfuse.util.TypeMirrorUtil;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;

import static org.androidtransfuse.util.AnnotationUtil.checkBlank;
import static org.androidtransfuse.util.AnnotationUtil.checkDefault;

/**
 * Activity related Analysis
 *
 * @author John Ericksen
 */
public class ActivityAnalysis implements Analysis<ComponentDescriptor> {

    private InjectionPointFactory injectionPointFactory;
    private InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory;
    private InjectionNodeBuilderRepository injectionNodeBuilderRepository;
    private Provider<org.androidtransfuse.model.manifest.Activity> manifestActivityProvider;
    private ActivityComponentBuilderRepositoryFactory activityComponentBuilderRepository;
    private AnalysisContextFactory analysisContextFactory;
    private Provider<ASTTypeBuilderVisitor> astTypeBuilderVisitorProvider;
    private ASTClassFactory astClassFactory;
    private ManifestManager manifestManager;
    private IntentFilterFactory intentFilterBuilder;
    private TypeMirrorUtil typeMirrorUtil;
    private ComponentBuilderFactory componentBuilderFactory;
    private MetaDataBuilder metadataBuilder;
    private BindingRepositoryFactory bindingRepositoryFactory;
    private ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private InjectionBindingBuilder injectionBindingBuilder;
    private ContextScopeComponentBuilder contextScopeComponentBuilder;

    @Inject
    public ActivityAnalysis(InjectionPointFactory injectionPointFactory,
                            InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory,
                            InjectionNodeBuilderRepository injectionNodeBuilderRepository,
                            Provider<org.androidtransfuse.model.manifest.Activity> manifestActivityProvider,
                            ActivityComponentBuilderRepositoryFactory activityComponentBuilderRepository,
                            AnalysisContextFactory analysisContextFactory,
                            Provider<ASTTypeBuilderVisitor> astTypeBuilderVisitorProvider,
                            ASTClassFactory astClassFactory,
                            ManifestManager manifestManager,
                            IntentFilterFactory intentFilterBuilder,
                            TypeMirrorUtil typeMirrorUtil,
                            ComponentBuilderFactory componentBuilderFactory,
                            MetaDataBuilder metadataBuilder,
                            BindingRepositoryFactory bindingRepositoryFactory,
                            ASTTypeBuilderVisitor astTypeBuilderVisitor,
                            InjectionBindingBuilder injectionBindingBuilder, ContextScopeComponentBuilder contextScopeComponentBuilder) {
        this.injectionPointFactory = injectionPointFactory;
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
        this.injectionNodeBuilderRepository = injectionNodeBuilderRepository;
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
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.contextScopeComponentBuilder = contextScopeComponentBuilder;
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
            return inputPackageClass.append("Activity");
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

        ASTMethod onCreateASTMethod = getASTMethod("onCreate", Bundle.class);
        activityDescriptor.setMethodBuilder(componentBuilderFactory.buildOnCreateMethodBuilder(onCreateASTMethod, layoutBuilder));

        activityDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildInjectionNodeFactory(astType, context));

        activityDescriptor.addGenerators(activityComponentBuilderRepository.build(context).getGenerators(activityType));

        activityDescriptor.addGenerators(contextScopeComponentBuilder);

    }

    private ASTMethod getASTMethod(String methodName, Class... args) {
        return getASTMethod(android.app.Activity.class, methodName, args);
    }

    private ASTMethod getASTMethod(Class type, String methodName, Class... args) {
        try {
            return astClassFactory.buildASTClassMethod(type.getDeclaredMethod(methodName, args));
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while trying to reference method " + methodName, e);
        }
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(TypeMirror activityType) {

        injectionNodeBuilderRepository.putType(Context.class, injectionBindingBuilder.buildThis(Context.class));
        injectionNodeBuilderRepository.putType(Application.class, injectionBindingBuilder.dependency(Context.class).invoke(Application.class, "getApplication").build());
        injectionNodeBuilderRepository.putType(android.app.Activity.class, injectionBindingBuilder.buildThis(android.app.Activity.class));
        injectionNodeBuilderRepository.putType(ContextScopeHolder.class, injectionBindingBuilder.buildThis(ContextScopeHolder.class));

        if (activityType != null) {
            ASTType activityASTType = activityType.accept(astTypeBuilderVisitor, null);
            injectionNodeBuilderRepository.putType(activityASTType, injectionBindingBuilder.buildThis(activityASTType));
        }

        bindingRepositoryFactory.addBindingAnnotations(injectionNodeBuilderRepository);
        bindingRepositoryFactory.addViewBindingAnnotation(injectionNodeBuilderRepository);

        injectionNodeBuilderRepositoryFactory.addApplicationInjections(injectionNodeBuilderRepository);

        injectionNodeBuilderRepositoryFactory.addModuleConfiguration(injectionNodeBuilderRepository);

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