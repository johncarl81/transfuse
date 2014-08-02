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

import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.experiment.ScopesGeneration;
import org.androidtransfuse.experiment.generators.FragmentLayoutGenerator;
import org.androidtransfuse.experiment.generators.ObservesExpressionGenerator;
import org.androidtransfuse.experiment.generators.OnCreateInjectionGenerator;
import org.androidtransfuse.experiment.generators.SuperGenerator;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.ListenerRegistrationGenerator;
import org.androidtransfuse.gen.variableBuilder.*;
import org.androidtransfuse.util.AndroidLiterals;
import org.androidtransfuse.util.TypeMirrorRunnable;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;

import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * @author John Ericksen
 */
public class FragmentAnalysis implements Analysis<ComponentDescriptor> {

    private final ASTClassFactory astClassFactory;
    private final ASTElementFactory astElementFactory;
    private final AnalysisContextFactory analysisContextFactory;
    private final Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final ListenerRegistrationGenerator.ListerRegistrationGeneratorFactory listenerRegistrationGeneratorFactory;
    private final ObservesExpressionGenerator.ObservesExpressionGeneratorFactory observesExpressionGeneratorFactory;
    private final ExtraInjectionNodeBuilder extraInjectionNodeBuilder;
    private final SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder;
    private final ResourceInjectionNodeBuilder resourceInjectionNodeBuilder;
    private final PreferenceInjectionNodeBuilder preferenceInjectionNodeBuilder;
    private final FragmentViewInjectionNodeBuilder fragmentViewInjectionNodeBuilder;
    private final OnCreateInjectionGenerator.InjectionGeneratorFactory injectionGeneratorFactory;
    private final FragmentLayoutGenerator fragmentLayoutGenerator;
    private final ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory;
    private final JCodeModel codeModel;
    private final SuperGenerator.SuperGeneratorFactory superGeneratorFactory;

    @Inject
    public FragmentAnalysis(ASTClassFactory astClassFactory,
                            ASTElementFactory astElementFactory,
                            AnalysisContextFactory analysisContextFactory,
                            Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider,
                            InjectionBindingBuilder injectionBindingBuilder,
                            ASTTypeBuilderVisitor astTypeBuilderVisitor,
                            InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory,
                            ComponentBuilderFactory componentBuilderFactory,
                            ListenerRegistrationGenerator.ListerRegistrationGeneratorFactory listenerRegistrationGeneratorFactory,
                            ObservesExpressionGenerator.ObservesExpressionGeneratorFactory observesExpressionGeneratorFactory,
                            ExtraInjectionNodeBuilder extraInjectionNodeBuilder,
                            SystemServiceBindingInjectionNodeBuilder systemServiceBindingInjectionNodeBuilder,
                            ResourceInjectionNodeBuilder resourceInjectionNodeBuilder,
                            PreferenceInjectionNodeBuilder preferenceInjectionNodeBuilder,
                            FragmentViewInjectionNodeBuilder fragmentViewInjectionNodeBuilder,
                            OnCreateInjectionGenerator.InjectionGeneratorFactory injectionGeneratorFactory,
                            FragmentLayoutGenerator fragmentLayoutGenerator,
                            ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory,
                            JCodeModel codeModel,
                            SuperGenerator.SuperGeneratorFactory superGeneratorFactory) {
        this.astClassFactory = astClassFactory;
        this.astElementFactory = astElementFactory;
        this.analysisContextFactory = analysisContextFactory;
        this.injectionNodeBuilderRepositoryProvider = injectionNodeBuilderRepositoryProvider;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
        this.componentBuilderFactory = componentBuilderFactory;
        this.extraInjectionNodeBuilder = extraInjectionNodeBuilder;
        this.systemServiceBindingInjectionNodeBuilder = systemServiceBindingInjectionNodeBuilder;
        this.resourceInjectionNodeBuilder = resourceInjectionNodeBuilder;
        this.preferenceInjectionNodeBuilder = preferenceInjectionNodeBuilder;
        this.fragmentViewInjectionNodeBuilder = fragmentViewInjectionNodeBuilder;
        this.listenerRegistrationGeneratorFactory = listenerRegistrationGeneratorFactory;
        this.observesExpressionGeneratorFactory = observesExpressionGeneratorFactory;
        this.injectionGeneratorFactory = injectionGeneratorFactory;
        this.fragmentLayoutGenerator = fragmentLayoutGenerator;
        this.scopesGenerationFactory = scopesGenerationFactory;
        this.codeModel = codeModel;
        this.superGeneratorFactory = superGeneratorFactory;
    }

    @Override
    public ComponentDescriptor analyze(ASTType astType) {
        Fragment fragmentAnnotation = astType.getAnnotation(Fragment.class);
        PackageClass fragmentClassName;
        ComponentDescriptor fragmentDescriptor = null;

        if (!astType.extendsFrom(AndroidLiterals.FRAGMENT)) {
            //generated Android fragment
            fragmentClassName = buildPackageClass(astType, fragmentAnnotation.name());

            TypeMirror type = getTypeMirror(new FragmentTypeMirrorRunnable(fragmentAnnotation));

            ASTType fragmentType = type == null  || type.toString().equals("java.lang.Object") ? AndroidLiterals.FRAGMENT : type.accept(astTypeBuilderVisitor, null);

            AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(type));

            fragmentDescriptor = new ComponentDescriptor(astType, fragmentType, fragmentClassName);

            //application generation profile
            setupFragmentProfile(fragmentDescriptor, astType, fragmentType, context);
        }

        return fragmentDescriptor;
    }

    private void setupFragmentProfile(ComponentDescriptor fragmentDescriptor, ASTType astType, ASTType fragmentType, AnalysisContext context) {

        ASTMethod onCreateViewMethod = getASTMethod("onCreateView", AndroidLiterals.LAYOUT_INFLATER, AndroidLiterals.VIEW_GROUP, AndroidLiterals.BUNDLE);

        fragmentDescriptor.getGenerators().add(injectionGeneratorFactory.build(onCreateViewMethod, astType));
        fragmentDescriptor.getGenerators().add(scopesGenerationFactory.build(onCreateViewMethod));

        fragmentDescriptor.getGenerators().add(fragmentLayoutGenerator);

        fragmentDescriptor.setAnalysisContext(context);
        fragmentDescriptor.getGenerateFirst().add(new MethodSignature(getASTMethod("onCreateView", AndroidLiterals.LAYOUT_INFLATER, AndroidLiterals.VIEW_GROUP, AndroidLiterals.BUNDLE)));

        fragmentDescriptor.getGenerators().add(buildEventMethod(OnCreate.class, "onCreateView", AndroidLiterals.LAYOUT_INFLATER, AndroidLiterals.VIEW_GROUP, AndroidLiterals.BUNDLE));
        //onActivityCreated
        fragmentDescriptor.getGenerators().add(buildEventMethod(OnActivityCreated.class, "onActivityCreated", AndroidLiterals.BUNDLE));
        fragmentDescriptor.getGenerators().add(superGeneratorFactory.build(getASTMethod("onActivityCreated", AndroidLiterals.BUNDLE)));
        //onStart
        fragmentDescriptor.getGenerators().add(buildEventMethod(OnStart.class, "onStart"));
        fragmentDescriptor.getGenerators().add(superGeneratorFactory.build(getASTMethod("onStart")));
        //onResume
        fragmentDescriptor.getGenerators().add(buildEventMethod(OnResume.class, "onResume"));
        fragmentDescriptor.getGenerators().add(superGeneratorFactory.build(getASTMethod("onResume")));
        //onPause
        fragmentDescriptor.getGenerators().add(buildEventMethod(OnPause.class, "onPause"));
        fragmentDescriptor.getGenerators().add(superGeneratorFactory.build(getASTMethod("onPause")));
        //onStop
        fragmentDescriptor.getGenerators().add(buildEventMethod(OnStop.class, "onStop"));
        fragmentDescriptor.getGenerators().add(superGeneratorFactory.build(getASTMethod("onStop")));
        //onDestroyView
        fragmentDescriptor.getGenerators().add(buildEventMethod(OnDestroyView.class, "onDestroyView"));
        fragmentDescriptor.getGenerators().add(superGeneratorFactory.build(getASTMethod("onDestroyView")));
        //onDestroy
        fragmentDescriptor.getGenerators().add(buildEventMethod(OnDestroy.class, "onDestroy"));
        fragmentDescriptor.getGenerators().add(superGeneratorFactory.build(getASTMethod("onDestroy")));
        //onDetach
        fragmentDescriptor.getGenerators().add(buildEventMethod(OnDetach.class, "onDetach"));
        fragmentDescriptor.getGenerators().add(superGeneratorFactory.build(getASTMethod("onDetach")));
        //onLowMemory
        fragmentDescriptor.getGenerators().add(buildEventMethod(OnLowMemory.class, "onLowMemory"));
        fragmentDescriptor.getGenerators().add(superGeneratorFactory.build(getASTMethod("onLowMemory")));
        //onActivityResult
        fragmentDescriptor.getGenerators().add(buildEventMethod(OnActivityResult.class, "onActivityResult", ASTPrimitiveType.INT, ASTPrimitiveType.INT, AndroidLiterals.INTENT));

        //onConfigurationChanged
        fragmentDescriptor.getGenerators().add(buildEventMethod(OnConfigurationChanged.class, "onConfigurationChanged", AndroidLiterals.CONTENT_CONFIGURATION));
        fragmentDescriptor.getGenerators().add(superGeneratorFactory.build(getASTMethod("onConfigurationChanged", AndroidLiterals.CONTENT_CONFIGURATION)));

        if (fragmentType.extendsFrom(AndroidLiterals.LIST_FRAGMENT)) {
            ASTMethod onListItemClickMethod = getASTMethod(AndroidLiterals.LIST_ACTIVITY, "onListItemClick", AndroidLiterals.LIST_VIEW, AndroidLiterals.VIEW, ASTPrimitiveType.INT, ASTPrimitiveType.LONG);
            fragmentDescriptor.getGenerators().add(
                    componentBuilderFactory.buildMethodCallbackGenerator(astClassFactory.getType(OnListItemClick.class), onListItemClickMethod, getASTMethod("onCreateView", AndroidLiterals.LAYOUT_INFLATER, AndroidLiterals.VIEW_GROUP, AndroidLiterals.BUNDLE))
            );
        }

        // onSaveInstanceState
        ASTMethod onSaveInstanceStateMethod = getASTMethod("onSaveInstanceState", AndroidLiterals.BUNDLE);
        fragmentDescriptor.getGenerators().add(componentBuilderFactory.buildFragmentOnSaveInstanceStateMethodCallbackGenerator(astClassFactory.getType(OnSaveInstanceState.class), onSaveInstanceStateMethod, getASTMethod("onCreateView", AndroidLiterals.LAYOUT_INFLATER, AndroidLiterals.VIEW_GROUP, AndroidLiterals.BUNDLE)));

        fragmentDescriptor.getGenerators().add(listenerRegistrationGeneratorFactory.build(getASTMethod("onCreateView", AndroidLiterals.LAYOUT_INFLATER, AndroidLiterals.VIEW_GROUP, AndroidLiterals.BUNDLE)));

        fragmentDescriptor.getGenerators().add(observesExpressionGeneratorFactory.build(
                getASTMethod("onCreateView", AndroidLiterals.LAYOUT_INFLATER, AndroidLiterals.VIEW_GROUP, AndroidLiterals.BUNDLE),
                getASTMethod("onResume"),
                getASTMethod("onPause")
        ));

    }

    private org.androidtransfuse.experiment.generators.MethodCallbackGenerator buildEventMethod(Class<? extends Annotation> eventAnnotationClass, String methodName, ASTType... args) {
        ASTMethod method = getASTMethod(methodName, args);
        ASTType eventAnnotation = astClassFactory.getType(eventAnnotationClass);

        return componentBuilderFactory.buildMethodCallbackGenerator(eventAnnotation, method, getASTMethod("onCreateView", AndroidLiterals.LAYOUT_INFLATER, AndroidLiterals.VIEW_GROUP, AndroidLiterals.BUNDLE));
    }

    private ASTMethod getASTMethod(String methodName, ASTType... args) {
        return getASTMethod(AndroidLiterals.FRAGMENT, methodName, args);
    }

    private ASTMethod getASTMethod(ASTType type, String methodName, ASTType... args) {
        return astElementFactory.findMethod(type, methodName, args);
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(TypeMirror type) {

        InjectionNodeBuilderRepository injectionNodeBuilderRepository = injectionNodeBuilderRepositoryProvider.get();

        injectionNodeBuilderRepository.putType(AndroidLiterals.FRAGMENT, injectionBindingBuilder.buildThis(AndroidLiterals.FRAGMENT));
        injectionNodeBuilderRepository.putType(AndroidLiterals.ACTIVITY, injectionBindingBuilder.dependency(AndroidLiterals.FRAGMENT).invoke(AndroidLiterals.ACTIVITY, "getActivity").build());
        injectionNodeBuilderRepository.putType(AndroidLiterals.CONTEXT, injectionBindingBuilder.dependency(AndroidLiterals.FRAGMENT).invoke(AndroidLiterals.CONTEXT, "getActivity").build());
        injectionNodeBuilderRepository.putType(AndroidLiterals.FRAGMENT_MANAGER, injectionBindingBuilder.dependency(AndroidLiterals.FRAGMENT).invoke(AndroidLiterals.FRAGMENT_MANAGER, "getFragmentManager").build());
        injectionNodeBuilderRepository.putType(AndroidLiterals.APPLICATION, injectionBindingBuilder.dependency(AndroidLiterals.ACTIVITY).invoke(AndroidLiterals.APPLICATION, "getApplication").build());


        if(type != null){
            ASTType fragmentASTType = type.accept(astTypeBuilderVisitor, null);

            while(!fragmentASTType.equals(AndroidLiterals.FRAGMENT) && fragmentASTType.inheritsFrom(AndroidLiterals.FRAGMENT)){
                injectionNodeBuilderRepository.putType(fragmentASTType, injectionBindingBuilder.buildThis(fragmentASTType));
                fragmentASTType = fragmentASTType.getSuperClass();
            }
        }

        injectionNodeBuilderRepository.putAnnotation(Extra.class, extraInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(Resource.class, resourceInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(SystemService.class, systemServiceBindingInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(Preference.class, preferenceInjectionNodeBuilder);
        injectionNodeBuilderRepository.putAnnotation(org.androidtransfuse.annotations.View.class, fragmentViewInjectionNodeBuilder);

        injectionNodeBuilderRepository.addRepository(injectionNodeBuilderRepositoryFactory.buildApplicationInjections());
        injectionNodeBuilderRepository.addRepository(injectionNodeBuilderRepositoryFactory.buildModuleConfiguration());

        return injectionNodeBuilderRepository;
    }

    private PackageClass buildPackageClass(ASTType input, String fragmentName) {

        PackageClass inputPackageClass = input.getPackageClass();

        if (StringUtils.isBlank(fragmentName)) {
            return inputPackageClass.append("Fragment");
        } else {
            return inputPackageClass.replaceName(fragmentName);
        }
    }

    private static final class FragmentTypeMirrorRunnable extends TypeMirrorRunnable<Fragment> {
        public FragmentTypeMirrorRunnable(Fragment fragmentAnnotation) {
            super(fragmentAnnotation);
        }

        @Override
        public void run(Fragment annotation) {
            annotation.type();
        }


    }
}
