/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.analysis;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.ActivityComponentBuilderRepositoryFactory;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.componentBuilder.*;
import org.androidtransfuse.gen.variableBuilder.*;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.processor.ManifestManager;
import org.androidtransfuse.scope.ContextScopeHolder;
import org.androidtransfuse.util.TypeMirrorRunnable;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;

import static org.androidtransfuse.util.AnnotationUtil.checkBlank;
import static org.androidtransfuse.util.AnnotationUtil.checkDefault;
import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * Activity related Analysis
 *
 * @author John Ericksen
 */
public class ActivityAnalysis implements Analysis<ComponentDescriptor> {

    private final InjectionPointFactory injectionPointFactory;
    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory;
    private final Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider;
    private final Provider<org.androidtransfuse.model.manifest.Activity> manifestActivityProvider;
    private final ActivityComponentBuilderRepositoryFactory activityComponentBuilderRepository;
    private final AnalysisContextFactory analysisContextFactory;
    private final Provider<ASTTypeBuilderVisitor> astTypeBuilderVisitorProvider;
    private final ASTClassFactory astClassFactory;
    private final ManifestManager manifestManager;
    private final IntentFilterFactory intentFilterBuilder;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final MetaDataBuilder metadataBuilder;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final ContextScopeComponentBuilder contextScopeComponentBuilder;
    private final ObservesRegistrationGenerator observesExpressionDecorator;
    private final ViewInjectionNodeBuilder viewVariableBuilder;
    private final ExtraInjectionNodeBuilder extraInjectionNodeBuilder;
    private final SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder;
    private final ResourceInjectionNodeBuilder resourceInjectionNodeBuilder;
    private final PreferenceInjectionNodeBuilder preferenceInjectionNodeBuilder;

    @Inject
    public ActivityAnalysis(InjectionPointFactory injectionPointFactory,
                            InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory,
                            Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider,
                            Provider<org.androidtransfuse.model.manifest.Activity> manifestActivityProvider,
                            ActivityComponentBuilderRepositoryFactory activityComponentBuilderRepository,
                            AnalysisContextFactory analysisContextFactory,
                            Provider<ASTTypeBuilderVisitor> astTypeBuilderVisitorProvider,
                            ASTClassFactory astClassFactory,
                            ManifestManager manifestManager,
                            IntentFilterFactory intentFilterBuilder,
                            ComponentBuilderFactory componentBuilderFactory,
                            MetaDataBuilder metadataBuilder,
                            ASTTypeBuilderVisitor astTypeBuilderVisitor,
                            InjectionBindingBuilder injectionBindingBuilder,
                            ContextScopeComponentBuilder contextScopeComponentBuilder,
                            ObservesRegistrationGenerator observesExpressionDecorator,
                            ViewInjectionNodeBuilder viewVariableBuilder,
                            ExtraInjectionNodeBuilder extraInjectionNodeBuilder,
                            SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder,
                            ResourceInjectionNodeBuilder resourceInjectionNodeBuilder,
                            PreferenceInjectionNodeBuilder preferenceInjectionNodeBuilder) {
        this.injectionPointFactory = injectionPointFactory;
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
        this.injectionNodeBuilderRepositoryProvider = injectionNodeBuilderRepositoryProvider;
        this.manifestActivityProvider = manifestActivityProvider;
        this.activityComponentBuilderRepository = activityComponentBuilderRepository;
        this.analysisContextFactory = analysisContextFactory;
        this.astTypeBuilderVisitorProvider = astTypeBuilderVisitorProvider;
        this.astClassFactory = astClassFactory;
        this.manifestManager = manifestManager;
        this.intentFilterBuilder = intentFilterBuilder;
        this.componentBuilderFactory = componentBuilderFactory;
        this.metadataBuilder = metadataBuilder;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.contextScopeComponentBuilder = contextScopeComponentBuilder;
        this.observesExpressionDecorator = observesExpressionDecorator;
        this.viewVariableBuilder = viewVariableBuilder;
        this.extraInjectionNodeBuilder = extraInjectionNodeBuilder;
        this.systemServiceBindingInjectionNodeBuilder = systemServiceBindingInjectionNodeBuilder;
        this.resourceInjectionNodeBuilder = resourceInjectionNodeBuilder;
        this.preferenceInjectionNodeBuilder = preferenceInjectionNodeBuilder;
    }

    public ComponentDescriptor analyze(ASTType input) {

        Activity activityAnnotation = input.getAnnotation(Activity.class);
        PackageClass activityClassName;
        ComponentDescriptor activityDescriptor = null;

        if (input.extendsFrom(astClassFactory.getType(android.app.Activity.class))) {
            //vanilla Android activity
            PackageClass activityPackageClass = input.getPackageClass();
            activityClassName = buildPackageClass(input, activityPackageClass.getClassName());
        } else {
            //generated Android activity
            activityClassName = buildPackageClass(input, activityAnnotation.name());

            Layout layoutAnnotation = input.getAnnotation(Layout.class);
            LayoutHandler layoutHandlerAnnotation = input.getAnnotation(LayoutHandler.class);

            TypeMirror type = getTypeMirror(new ActivityTypeMirrorRunnable(activityAnnotation));

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
            TypeMirror layoutHandlerType = getTypeMirror(new LayoutHandlerTypeMirrorRunnable(layoutHandlerAnnotation));

            if (layoutHandlerType != null) {
                ASTType layoutHandlerASTType = layoutHandlerType.accept(astTypeBuilderVisitorProvider.get(), null);
                return injectionPointFactory.buildInjectionNode(layoutHandlerASTType, context);
            }
        }
        return null;
    }

    private PackageClass buildPackageClass(ASTType input, String activityName) {

        PackageClass inputPackageClass = input.getPackageClass();

        if (StringUtils.isBlank(activityName)) {
            return inputPackageClass.append("Activity");
        } else {
            return inputPackageClass.replaceName(activityName);
        }
    }

    private void setupManifest(String name, Activity activityAnnotation, ASTType type) {
        org.androidtransfuse.model.manifest.Activity manifestActivity = buildManifestEntry(name, activityAnnotation);

        manifestActivity.setIntentFilters(intentFilterBuilder.buildIntentFilters(type));
        manifestActivity.setMetaData(metadataBuilder.buildMetaData(type));

        manifestManager.addActivity(manifestActivity);
    }

    protected org.androidtransfuse.model.manifest.Activity buildManifestEntry(String name, Activity activityAnnotation) {
        org.androidtransfuse.model.manifest.Activity manifestActivity = manifestActivityProvider.get();

        manifestActivity.setName(name);
        manifestActivity.setLabel(checkBlank(activityAnnotation.label()));
        manifestActivity.setAllowTaskReparenting(checkDefault(activityAnnotation.allowTaskReparenting(), false));
        manifestActivity.setAlwaysRetainTaskState(checkDefault(activityAnnotation.alwaysRetainTaskState(), false));
        manifestActivity.setClearTaskOnLaunch(checkDefault(activityAnnotation.clearTaskOnLaunch(), false));
        manifestActivity.setConfigChanges(concatenate(activityAnnotation.configChanges()));
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

        return manifestActivity;
    }

    private String concatenate(ConfigChanges[] configChanges) {
        if (configChanges.length == 0) {
            return null;
        }

        return StringUtils.join(configChanges, "|");
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
        activityDescriptor.setInitMethodBuilder(astClassFactory.getType(OnCreate.class), componentBuilderFactory.buildOnCreateMethodBuilder(onCreateASTMethod, layoutBuilder));

        activityDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildInjectionNodeFactory(ImmutableSet.<ASTAnnotation>of(), astType, context));

        activityDescriptor.addGenerators(activityComponentBuilderRepository.build().getGenerators(activityType));

        activityDescriptor.addGenerators(contextScopeComponentBuilder);

        activityDescriptor.addRegistration(observesExpressionDecorator);

    }

    private ASTMethod getASTMethod(String methodName, Class... args) {
        return getASTMethod(android.app.Activity.class, methodName, args);
    }

    private ASTMethod getASTMethod(Class type, String methodName, Class... args) {
        try {
            return astClassFactory.getMethod(type.getDeclaredMethod(methodName, args));
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while trying to reference method " + methodName, e);
        }
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(TypeMirror activityType) {

        InjectionNodeBuilderRepository injectionNodeBuilderRepository = injectionNodeBuilderRepositoryProvider.get();

        injectionNodeBuilderRepository.putType(Context.class, injectionBindingBuilder.buildThis(Context.class));
        injectionNodeBuilderRepository.putType(Application.class, injectionBindingBuilder.dependency(Context.class).invoke(Application.class, "getApplication").build());
        injectionNodeBuilderRepository.putType(android.app.Activity.class, injectionBindingBuilder.buildThis(android.app.Activity.class));
        injectionNodeBuilderRepository.putType(ContextScopeHolder.class, injectionBindingBuilder.buildThis(ContextScopeHolder.class));

        if (activityType != null && !activityType.toString().equals(android.app.Activity.class.getName())) {
            ASTType activityASTType = activityType.accept(astTypeBuilderVisitor, null);
            injectionNodeBuilderRepository.putType(activityASTType, injectionBindingBuilder.buildThis(activityASTType));
        }

        injectionNodeBuilderRepository.putAnnotation(Extra.class, extraInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(Resource.class, resourceInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(SystemService.class, systemServiceBindingInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(Preference.class, preferenceInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(View.class, viewVariableBuilder);

        injectionNodeBuilderRepository.addRepository(
                injectionNodeBuilderRepositoryFactory.buildApplicationInjections());

        injectionNodeBuilderRepository.addRepository(
                injectionNodeBuilderRepositoryFactory.buildModuleConfiguration());

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