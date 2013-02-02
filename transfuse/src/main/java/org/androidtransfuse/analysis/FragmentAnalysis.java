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

import android.app.Activity;
import android.app.Application;
import android.app.ListActivity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.BindingRepositoryFactory;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.ListenerRegistrationGenerator;
import org.androidtransfuse.gen.componentBuilder.MethodCallbackGenerator;
import org.androidtransfuse.gen.componentBuilder.ObservesRegistrationGenerator;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.scope.ContextScopeHolder;
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
    private final AnalysisContextFactory analysisContextFactory;
    private final Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final BindingRepositoryFactory bindingRepositoryFactory;
    private final ListenerRegistrationGenerator listenerRegistrationGenerator;
    private final ObservesRegistrationGenerator observesExpressionDecorator;

    @Inject
    public FragmentAnalysis(ASTClassFactory astClassFactory,
                            AnalysisContextFactory analysisContextFactory,
                            Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider,
                            InjectionBindingBuilder injectionBindingBuilder,
                            ASTTypeBuilderVisitor astTypeBuilderVisitor,
                            InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory,
                            ComponentBuilderFactory componentBuilderFactory,
                            BindingRepositoryFactory bindingRepositoryFactory,
                            ListenerRegistrationGenerator listenerRegistrationGenerator,
                            ObservesRegistrationGenerator observesExpressionDecorator) {
        this.astClassFactory = astClassFactory;
        this.analysisContextFactory = analysisContextFactory;
        this.injectionNodeBuilderRepositoryProvider = injectionNodeBuilderRepositoryProvider;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
        this.componentBuilderFactory = componentBuilderFactory;
        this.bindingRepositoryFactory = bindingRepositoryFactory;
        this.listenerRegistrationGenerator = listenerRegistrationGenerator;
        this.observesExpressionDecorator = observesExpressionDecorator;
    }

    @Override
    public ComponentDescriptor analyze(ASTType astType) {
        Fragment fragmentAnnotation = astType.getAnnotation(Fragment.class);
        PackageClass fragmentClassName;
        ComponentDescriptor fragmentDescriptor = null;

        if (!astType.extendsFrom(astClassFactory.getType(android.support.v4.app.Fragment.class))) {
            //generated Android fragment
            fragmentClassName = buildPackageClass(astType, fragmentAnnotation.name());

            Layout layoutAnnotation = astType.getAnnotation(Layout.class);
            //todo: fragment specific layout handler

            TypeMirror type = getTypeMirror(new FragmentTypeMirrorRunnable(fragmentAnnotation));

            ASTType fragmentType = type == null ? astClassFactory.getType(android.support.v4.app.Fragment.class)
                    : type.accept(astTypeBuilderVisitor, null);


            Integer layout = layoutAnnotation == null ? null : layoutAnnotation.value();

            AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(type));

            fragmentDescriptor = new ComponentDescriptor(fragmentType.getName(), fragmentClassName);

            //application generation profile
            setupFragmentProfile(fragmentDescriptor, astType, fragmentType, context, layout);
        }


        return fragmentDescriptor;

    }

    private void setupFragmentProfile(ComponentDescriptor fragmentDescriptor, ASTType astType, ASTType fragmentType, AnalysisContext context, Integer layout) {

        ASTMethod onCreateViewMethod = getASTMethod("onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class);

        fragmentDescriptor.setInitMethodBuilder(astClassFactory.getType(OnCreate.class), componentBuilderFactory.buildFragmentMethodBuilder(layout, onCreateViewMethod));

        fragmentDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildInjectionNodeFactory(astType, context));

        //onActivityCreated
        fragmentDescriptor.addGenerators(buildEventMethod(OnActivityCreated.class, "onActivityCreated", Bundle.class));
        //onStart
        fragmentDescriptor.addGenerators(buildEventMethod(OnStart.class, "onStart"));
        //onResume
        fragmentDescriptor.addGenerators(buildEventMethod(OnResume.class, "onResume"));
        //onPause
        fragmentDescriptor.addGenerators(buildEventMethod(OnPause.class, "onPause"));
        //onStop
        fragmentDescriptor.addGenerators(buildEventMethod(OnStop.class, "onStop"));
        //onDestroyView
        fragmentDescriptor.addGenerators(buildEventMethod(OnDestroyView.class, "onDestroyView"));
        //onDestroy
        fragmentDescriptor.addGenerators(buildEventMethod(OnDestroy.class, "onDestroy"));
        //onDetach
        fragmentDescriptor.addGenerators(buildEventMethod(OnDetach.class, "onDetach"));
        //onLowMemory
        fragmentDescriptor.addGenerators(buildEventMethod(OnLowMemory.class, "onLowMemory"));

        //onConfigurationChanged
        fragmentDescriptor.addGenerators(buildEventMethod(OnConfigurationChanged.class, "onConfigurationChanged", Configuration.class));

        if (fragmentType.extendsFrom(astClassFactory.getType(ListFragment.class))) {
            ASTMethod onListItemClickMethod = getASTMethod(ListActivity.class, "onListItemClick", ListView.class, View.class, Integer.TYPE, Long.TYPE);
            fragmentDescriptor.addGenerators(
                    componentBuilderFactory.buildMethodCallbackGenerator(astClassFactory.getType(OnListItemClick.class),
                            componentBuilderFactory.buildMirroredMethodGenerator(onListItemClickMethod, false)));
        }

        fragmentDescriptor.addGenerators(listenerRegistrationGenerator);

        fragmentDescriptor.addRegistration(observesExpressionDecorator);

    }

    private MethodCallbackGenerator buildEventMethod(Class<? extends Annotation> eventAnnotationClass, String methodName, Class... args) {
        ASTMethod method = getASTMethod(methodName, args);
        ASTType eventAnnotation = astClassFactory.getType(eventAnnotationClass);

        return componentBuilderFactory.buildMethodCallbackGenerator(eventAnnotation, componentBuilderFactory.buildMirroredMethodGenerator(method, true));
    }

    private ASTMethod getASTMethod(String methodName, Class... args) {
        return getASTMethod(android.support.v4.app.Fragment.class, methodName, args);
    }

    private ASTMethod getASTMethod(Class type, String methodName, Class... args) {
        try {
            return astClassFactory.getMethod(type.getDeclaredMethod(methodName, args));
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while trying to reference method " + methodName, e);
        }
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(TypeMirror type) {

        InjectionNodeBuilderRepository injectionNodeBuilderRepository = injectionNodeBuilderRepositoryProvider.get();

        injectionNodeBuilderRepository.putType(android.support.v4.app.Fragment.class, injectionBindingBuilder.buildThis(android.support.v4.app.Fragment.class));
        injectionNodeBuilderRepository.putType(Activity.class, injectionBindingBuilder.dependency(android.support.v4.app.Fragment.class).invoke(Activity.class, "getActivity").build());
        injectionNodeBuilderRepository.putType(Context.class, injectionBindingBuilder.dependency(android.support.v4.app.Fragment.class).invoke(Context.class, "getActivity").build());
        injectionNodeBuilderRepository.putType(FragmentManager.class, injectionBindingBuilder.dependency(android.support.v4.app.Fragment.class).invoke(FragmentManager.class, "getFragmentManager").build());
        injectionNodeBuilderRepository.putType(Application.class, injectionBindingBuilder.dependency(Activity.class).invoke(Application.class, "getApplication").build());
        injectionNodeBuilderRepository.putType(ContextScopeHolder.class, injectionBindingBuilder.dependency(android.support.v4.app.Fragment.class).invoke(Activity.class, "getActivity").build());

        if (type != null && !type.toString().equals(android.support.v4.app.Fragment.class.getName())) {
            ASTType fragmentASTType = type.accept(astTypeBuilderVisitor, null);
            injectionNodeBuilderRepository.putType(fragmentASTType, injectionBindingBuilder.buildThis(fragmentASTType));
        }

        bindingRepositoryFactory.addBindingAnnotations(injectionNodeBuilderRepository);
        //bind views
        bindingRepositoryFactory.addFragmentViewBindingAnnotation(injectionNodeBuilderRepository);

        injectionNodeBuilderRepositoryFactory.addApplicationInjections(injectionNodeBuilderRepository);

        injectionNodeBuilderRepositoryFactory.addModuleConfiguration(injectionNodeBuilderRepository);

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
