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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.experiment.Generation;
import org.androidtransfuse.experiment.ScopesGeneration;
import org.androidtransfuse.experiment.generators.*;
import org.androidtransfuse.gen.GeneratorFactory;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.ListenerRegistrationGenerator;
import org.androidtransfuse.gen.componentBuilder.NonConfigurationInstanceGenerator;
import org.androidtransfuse.gen.variableBuilder.*;
import org.androidtransfuse.intentFactory.ActivityIntentFactoryStrategy;
import org.androidtransfuse.scope.ContextScopeHolder;
import org.androidtransfuse.util.AndroidLiterals;
import org.androidtransfuse.util.TypeMirrorRunnable;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Set;

import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * Activity related Analysis
 *
 * @author John Ericksen
 */
public class ActivityAnalysis implements Analysis<ComponentDescriptor> {

    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory;
    private final Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider;
    private final AnalysisContextFactory analysisContextFactory;
    private final ASTClassFactory astClassFactory;
    private final ASTElementFactory astElementFactory;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final ObservesExpressionGenerator.ObservesExpressionGeneratorFactory observesExpressionGeneratorFactory;
    private final ViewInjectionNodeBuilder viewVariableBuilder;
    private final ExtraInjectionNodeBuilder extraInjectionNodeBuilder;
    private final SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder;
    private final ResourceInjectionNodeBuilder resourceInjectionNodeBuilder;
    private final PreferenceInjectionNodeBuilder preferenceInjectionNodeBuilder;
    private final Provider<ActivityManifestEntryGenerator> manifestGeneratorProvider;
    private final LayoutGenerator layoutGenerator;
    private final LayoutHandlerGenerator layoutHandlerGenerator;
    private final SuperGenerator.SuperGeneratorFactory superGeneratorFactory;
    private final WindowFeatureGenerator windowFeatureGenerator;
    private final GeneratorFactory generatorFactory;
    private final ListenerRegistrationGenerator.ListerRegistrationGeneratorFactory listerRegistrationGeneratorFactory;
    private final NonConfigurationInstanceGenerator.NonconfigurationInstanceGeneratorFactory nonConfigurationInstanceGeneratorFactory;
    private final OnCreateInjectionGenerator.InjectionGeneratorFactory onCreateInjectionGeneratorFactory;
    private final ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory;

    @Inject
    public ActivityAnalysis(InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory,
                            Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider,
                            AnalysisContextFactory analysisContextFactory,
                            ASTClassFactory astClassFactory,
                            ASTElementFactory astElementFactory,
                            ComponentBuilderFactory componentBuilderFactory,
                            ASTTypeBuilderVisitor astTypeBuilderVisitor,
                            InjectionBindingBuilder injectionBindingBuilder,
                            ObservesExpressionGenerator.ObservesExpressionGeneratorFactory observesExpressionGeneratorFactory,
                            ViewInjectionNodeBuilder viewVariableBuilder,
                            ExtraInjectionNodeBuilder extraInjectionNodeBuilder,
                            SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder,
                            ResourceInjectionNodeBuilder resourceInjectionNodeBuilder,
                            PreferenceInjectionNodeBuilder preferenceInjectionNodeBuilder,
                            Provider<ActivityManifestEntryGenerator> manifestGeneratorProvider,
                            LayoutGenerator layoutGenerator,
                            LayoutHandlerGenerator layoutHandlerGenerator,
                            SuperGenerator.SuperGeneratorFactory superGeneratorFactory,
                            WindowFeatureGenerator windowFeatureGenerator,
                            GeneratorFactory generatorFactory,
                            ListenerRegistrationGenerator.ListerRegistrationGeneratorFactory listerRegistrationGeneratorFactory,
                            NonConfigurationInstanceGenerator.NonconfigurationInstanceGeneratorFactory nonConfigurationInstanceGeneratorFactory,
                            OnCreateInjectionGenerator.InjectionGeneratorFactory onCreateInjectionGeneratorFactory,
                            ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory) {
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
        this.injectionNodeBuilderRepositoryProvider = injectionNodeBuilderRepositoryProvider;
        this.analysisContextFactory = analysisContextFactory;
        this.astClassFactory = astClassFactory;
        this.astElementFactory = astElementFactory;
        this.componentBuilderFactory = componentBuilderFactory;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.observesExpressionGeneratorFactory = observesExpressionGeneratorFactory;
        this.viewVariableBuilder = viewVariableBuilder;
        this.extraInjectionNodeBuilder = extraInjectionNodeBuilder;
        this.systemServiceBindingInjectionNodeBuilder = systemServiceBindingInjectionNodeBuilder;
        this.resourceInjectionNodeBuilder = resourceInjectionNodeBuilder;
        this.preferenceInjectionNodeBuilder = preferenceInjectionNodeBuilder;
        this.manifestGeneratorProvider = manifestGeneratorProvider;
        this.layoutGenerator = layoutGenerator;
        this.layoutHandlerGenerator = layoutHandlerGenerator;
        this.superGeneratorFactory = superGeneratorFactory;
        this.windowFeatureGenerator = windowFeatureGenerator;
        this.generatorFactory = generatorFactory;
        this.listerRegistrationGeneratorFactory = listerRegistrationGeneratorFactory;
        this.nonConfigurationInstanceGeneratorFactory = nonConfigurationInstanceGeneratorFactory;
        this.onCreateInjectionGeneratorFactory = onCreateInjectionGeneratorFactory;
        this.scopesGenerationFactory = scopesGenerationFactory;
    }

    public ComponentDescriptor analyze(ASTType input) {

        Activity activityAnnotation = input.getAnnotation(Activity.class);
        PackageClass activityClassName;
        ComponentDescriptor activityDescriptor;

        if (input.extendsFrom(AndroidLiterals.ACTIVITY)) {
            //vanilla Android activity
            PackageClass activityPackageClass = input.getPackageClass();
            activityClassName = buildPackageClass(input, activityPackageClass.getClassName());
            activityDescriptor = new ComponentDescriptor(input, null, activityClassName);
        } else {
            //generated Android activity
            activityClassName = buildPackageClass(input, activityAnnotation.name());

            TypeMirror type = getTypeMirror(new ActivityTypeMirrorRunnable(activityAnnotation));

            ASTType activityType = type == null || type.toString().equals("java.lang.Object") ? AndroidLiterals.ACTIVITY : type.accept(astTypeBuilderVisitor, null);

            AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(type));

            activityDescriptor = new ComponentDescriptor(input, activityType, activityClassName);

            activityDescriptor.getGenerators().add(superGeneratorFactory.build(getASTMethod("onCreate", AndroidLiterals.BUNDLE)));
            activityDescriptor.getGenerators().add(layoutGenerator);
            activityDescriptor.getGenerators().add(layoutHandlerGenerator);
            activityDescriptor.getGenerators().add(windowFeatureGenerator);
            activityDescriptor.getGenerators().add(scopesGenerationFactory.build(getASTMethod("onCreate", AndroidLiterals.BUNDLE)));

            activityDescriptor.getGenerateFirst().add(new MethodSignature(getASTMethod("onCreate", AndroidLiterals.BUNDLE)));
            activityDescriptor.getGenerators().add(onCreateInjectionGeneratorFactory.build(getASTMethod("onCreate", AndroidLiterals.BUNDLE), input));

            activityDescriptor.setAnalysisContext(context);

            for (Generation generator : getGenerators(activityType.getName())) {
                activityDescriptor.getGenerators().add(generator);
            }

            //todo: proper context scoping activityDescriptor.addGenerators(contextScopeComponentBuilder);

            activityDescriptor.getGenerators().add(observesExpressionGeneratorFactory.build(
                    getASTMethod("onCreate", AndroidLiterals.BUNDLE),
                    getASTMethod("onResume"),
                    getASTMethod("onPause")));
        }

        //add manifest elements
        activityDescriptor.getGenerators().add(manifestGeneratorProvider.get());

        return activityDescriptor;
    }

    private PackageClass buildPackageClass(ASTType input, String activityName) {

        PackageClass inputPackageClass = input.getPackageClass();

        if (StringUtils.isBlank(activityName)) {
            return inputPackageClass.append("Activity");
        } else {
            return inputPackageClass.replaceName(activityName);
        }
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(TypeMirror activityType) {

        InjectionNodeBuilderRepository injectionNodeBuilderRepository = injectionNodeBuilderRepositoryProvider.get();

        injectionNodeBuilderRepository.putType(AndroidLiterals.CONTEXT, injectionBindingBuilder.buildThis(AndroidLiterals.CONTEXT));
        injectionNodeBuilderRepository.putType(AndroidLiterals.APPLICATION, injectionBindingBuilder.dependency(AndroidLiterals.CONTEXT).invoke(AndroidLiterals.APPLICATION, "getApplication").build());
        injectionNodeBuilderRepository.putType(AndroidLiterals.ACTIVITY, injectionBindingBuilder.buildThis(AndroidLiterals.ACTIVITY));
        injectionNodeBuilderRepository.putType(ContextScopeHolder.class, injectionBindingBuilder.buildThis(ContextScopeHolder.class));

        if(activityType != null){
            ASTType activityASTType = activityType.accept(astTypeBuilderVisitor, null);

            while(!activityASTType.equals(AndroidLiterals.ACTIVITY) && activityASTType.inheritsFrom(AndroidLiterals.ACTIVITY)){
                injectionNodeBuilderRepository.putType(activityASTType, injectionBindingBuilder.buildThis(activityASTType));
                activityASTType = activityASTType.getSuperClass();
            }
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

    public Set<Generation> getGenerators(String key) {

        ImmutableMap.Builder<String, ImmutableSet<Generation>> methodCallbackGenerators = ImmutableMap.builder();

        ImmutableSet<Generation> activityMethodGenerators = buildActivityMethodCallbackGenerators();
        methodCallbackGenerators.put(AndroidLiterals.ACTIVITY.getName(), activityMethodGenerators);
        methodCallbackGenerators.put(AndroidLiterals.LIST_ACTIVITY.getName(), buildListActivityMethodCallbackGenerators(activityMethodGenerators));
        methodCallbackGenerators.put(AndroidLiterals.PREFERENCE_ACTIVITY.getName(), activityMethodGenerators);
        methodCallbackGenerators.put(AndroidLiterals.ACTIVITY_GROUP.getName(), activityMethodGenerators);

        ImmutableMap<String, ImmutableSet<Generation>> map = methodCallbackGenerators.build();

        if(map.containsKey(key)){
            return map.get(key);
        }
        return map.get(AndroidLiterals.ACTIVITY.getName());

    }

    private ImmutableSet<Generation> buildListActivityMethodCallbackGenerators(Set<Generation> activityMethodGenerators) {
        ImmutableSet.Builder<Generation> listActivityCallbackGenerators = ImmutableSet.builder();
        listActivityCallbackGenerators.addAll(activityMethodGenerators);

        //onListItemClick(android.widget.ListView l, android.view.View v, int position, long id)
        ASTMethod onListItemClickMethod = getASTMethod(AndroidLiterals.LIST_ACTIVITY, "onListItemClick", AndroidLiterals.LIST_VIEW, AndroidLiterals.VIEW, ASTPrimitiveType.INT, ASTPrimitiveType.LONG);
        listActivityCallbackGenerators.add(
                componentBuilderFactory.buildMethodCallbackGenerator(astClassFactory.getType(OnListItemClick.class), onListItemClickMethod, getASTMethod("onCreate", AndroidLiterals.BUNDLE)));

        return listActivityCallbackGenerators.build();
    }

    private ImmutableSet<Generation> buildActivityMethodCallbackGenerators() {
        ImmutableSet.Builder<Generation> activityCallbackGenerators = ImmutableSet.builder();
        activityCallbackGenerators.add(buildEventMethod(OnCreate.class, "onCreate", AndroidLiterals.BUNDLE));
        // onDestroy
        activityCallbackGenerators.add(buildEventMethod(OnDestroy.class, "onDestroy"));
        activityCallbackGenerators.add(superGeneratorFactory.build(getASTMethod("onDestroy")));
        // onPause
        activityCallbackGenerators.add(buildEventMethod(OnPause.class, "onPause"));
        activityCallbackGenerators.add(superGeneratorFactory.build(getASTMethod("onPause")));
        // onRestart
        activityCallbackGenerators.add(buildEventMethod(OnRestart.class, "onRestart"));
        activityCallbackGenerators.add(superGeneratorFactory.build(getASTMethod("onRestart")));
        // onResume
        activityCallbackGenerators.add(buildEventMethod(OnResume.class, "onResume"));
        activityCallbackGenerators.add(superGeneratorFactory.build(getASTMethod("onResume")));
        // onStart
        activityCallbackGenerators.add(buildEventMethod(OnStart.class, "onStart"));
        activityCallbackGenerators.add(superGeneratorFactory.build(getASTMethod("onStart")));
        // onStop
        activityCallbackGenerators.add(buildEventMethod(OnStop.class, "onStop"));
        activityCallbackGenerators.add(superGeneratorFactory.build(getASTMethod("onStop")));
        // onBackPressed
        activityCallbackGenerators.add(buildEventMethod(OnBackPressed.class, "onBackPressed"));
        activityCallbackGenerators.add(superGeneratorFactory.build(getASTMethod("onBackPressed")));
        // onPostCreate
        activityCallbackGenerators.add(buildEventMethod(OnPostCreate.class, "onPostCreate", AndroidLiterals.BUNDLE));
        activityCallbackGenerators.add(superGeneratorFactory.build(getASTMethod("onPostCreate", AndroidLiterals.BUNDLE)));
        // onActivityResult
        activityCallbackGenerators.add(buildEventMethod(OnActivityResult.class, "onActivityResult", ASTPrimitiveType.INT, ASTPrimitiveType.INT, AndroidLiterals.INTENT));
        // onNewIntent
        activityCallbackGenerators.add(buildEventMethod(OnNewIntent.class, "onNewIntent", AndroidLiterals.INTENT));

        // onSaveInstanceState
        ASTMethod onSaveInstanceStateMethod = getASTMethod("onSaveInstanceState", AndroidLiterals.BUNDLE);
        activityCallbackGenerators.add(
                componentBuilderFactory.buildMethodCallbackGenerator(astClassFactory.getType(OnSaveInstanceState.class), onSaveInstanceStateMethod, getASTMethod("onCreate", AndroidLiterals.BUNDLE)));

        // onRestoreInstanceState
        ASTMethod onRestoreInstanceState = getASTMethod("onRestoreInstanceState", AndroidLiterals.BUNDLE);
        activityCallbackGenerators.add(
                componentBuilderFactory.buildMethodCallbackGenerator(astClassFactory.getType(OnRestoreInstanceState.class), onRestoreInstanceState, getASTMethod("onCreate", AndroidLiterals.BUNDLE)));

        //extra intent factory
        activityCallbackGenerators.add(generatorFactory.buildStrategyGenerator(ActivityIntentFactoryStrategy.class));

        //listener registration
        activityCallbackGenerators.add(listerRegistrationGeneratorFactory.build(getASTMethod("onCreate", AndroidLiterals.BUNDLE)));

        //non configuration instance update
        activityCallbackGenerators.add(nonConfigurationInstanceGeneratorFactory.build(getASTMethod("onCreate", AndroidLiterals.BUNDLE)));

        return activityCallbackGenerators.build();
    }

    private MethodCallbackGenerator buildEventMethod(Class<? extends Annotation> eventAnnotationClass, String methodName, ASTType... args) {

        ASTMethod method = getASTMethod(methodName, args);
        ASTType eventAnnotation = astClassFactory.getType(eventAnnotationClass);

        return componentBuilderFactory.buildMethodCallbackGenerator(eventAnnotation, method, getASTMethod("onCreate", AndroidLiterals.BUNDLE));
    }

    private ASTMethod getASTMethod(String methodName, ASTType... args) {
        return getASTMethod(AndroidLiterals.ACTIVITY, methodName, args);
    }

    private ASTMethod getASTMethod(ASTType type, String methodName, ASTType... args) {
        return astElementFactory.findMethod(type, methodName, args);
    }
}