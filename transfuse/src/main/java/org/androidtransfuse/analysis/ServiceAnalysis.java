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

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpr;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.MethodSignature;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.experiment.generators.ObservesExpressionGenerator;
import org.androidtransfuse.experiment.generators.OnCreateInjectionGenerator;
import org.androidtransfuse.experiment.generators.ServiceManifestEntryGenerator;
import org.androidtransfuse.gen.GeneratorFactory;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.ListenerRegistrationGenerator;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.intentFactory.ServiceIntentFactoryStrategy;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.scope.ContextScopeHolder;
import org.androidtransfuse.util.AndroidLiterals;
import org.androidtransfuse.util.TypeMirrorRunnable;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;

import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * Service related Analysis
 *
 * @author John Ericksen
 */
public class ServiceAnalysis implements Analysis<ComponentDescriptor> {

    private final Provider<InjectionNodeBuilderRepository> injectionNodeRepositoryProvider;
    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final AnalysisContextFactory analysisContextFactory;
    private final ASTClassFactory astClassFactory;
    private final ASTElementFactory astElementFactory;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final GeneratorFactory generatorFactory;
    private final ListenerRegistrationGenerator.ListerRegistrationGeneratorFactory listenerRegistrationGeneratorFactory;
    private final ObservesExpressionGenerator.ObservesExpressionGeneratorFactory observesExpressionDecoratorFactory;
    private final ServiceManifestEntryGenerator serviceManifestEntryGenerator;
    private final OnCreateInjectionGenerator.InjectionGeneratorFactory onCreateInjectionGeneratorFactory;
    private final ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory;

    @Inject
    public ServiceAnalysis(Provider<InjectionNodeBuilderRepository> injectionNodeRepositoryProvider,
                           InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory,
                           ComponentBuilderFactory componentBuilderFactory,
                           AnalysisContextFactory analysisContextFactory,
                           ASTClassFactory astClassFactory,
                           ASTElementFactory astElementFactory,
                           InjectionBindingBuilder injectionBindingBuilder,
                           ASTTypeBuilderVisitor astTypeBuilderVisitor,
                           GeneratorFactory generatorFactory,
                           ListenerRegistrationGenerator.ListerRegistrationGeneratorFactory listenerRegistrationGeneratorFactory,
                           ObservesExpressionGenerator.ObservesExpressionGeneratorFactory observesExpressionDecoratorFactory,
                           ServiceManifestEntryGenerator serviceManifestEntryGenerator,
                           OnCreateInjectionGenerator.InjectionGeneratorFactory onCreateInjectionGeneratorFactory,
                           ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory) {
        this.injectionNodeRepositoryProvider = injectionNodeRepositoryProvider;
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
        this.componentBuilderFactory = componentBuilderFactory;
        this.analysisContextFactory = analysisContextFactory;
        this.astClassFactory = astClassFactory;
        this.astElementFactory = astElementFactory;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.generatorFactory = generatorFactory;
        this.listenerRegistrationGeneratorFactory = listenerRegistrationGeneratorFactory;
        this.observesExpressionDecoratorFactory = observesExpressionDecoratorFactory;
        this.serviceManifestEntryGenerator = serviceManifestEntryGenerator;
        this.onCreateInjectionGeneratorFactory = onCreateInjectionGeneratorFactory;
        this.scopesGenerationFactory = scopesGenerationFactory;
    }

    public ComponentDescriptor analyze(ASTType input) {

        Service serviceAnnotation = input.getAnnotation(Service.class);
        PackageClass serviceClassName;
        ComponentDescriptor serviceDescriptor = null;

        if (input.extendsFrom(AndroidLiterals.SERVICE)) {
            //vanilla Android Service
            PackageClass packageClass = input.getPackageClass();
            serviceClassName = buildPackageClass(input, packageClass.getClassName());

            serviceDescriptor = new ComponentDescriptor(input, null, packageClass);
        } else {
            //generated Android Service
            serviceClassName = buildPackageClass(input, serviceAnnotation.name());

            TypeMirror type = getTypeMirror(new ServiceTypeMirrorRunnable(serviceAnnotation));

            ASTType serviceType = type == null || type.toString().equals("java.lang.Object") ? AndroidLiterals.SERVICE : type.accept(astTypeBuilderVisitor, null);

            AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(type));

            serviceDescriptor = new ComponentDescriptor(input, serviceType, serviceClassName);

            //application generation profile
            setupServiceProfile(serviceDescriptor, input, context);
        }

        serviceDescriptor.getGenerators().add(serviceManifestEntryGenerator);

        return serviceDescriptor;
    }

    private PackageClass buildPackageClass(ASTType input, String activityName) {

        PackageClass inputPackageClass = input.getPackageClass();

        if (StringUtils.isBlank(activityName)) {
            return inputPackageClass.append("Service");
        } else {
            return inputPackageClass.replaceName(activityName);
        }
    }

    private void setupServiceProfile(ComponentDescriptor serviceDescriptor, ASTType astType, AnalysisContext context) {

        serviceDescriptor.getGenerateFirst().add(new MethodSignature(getASTMethod("onCreate")));
        serviceDescriptor.getGenerators().add(onCreateInjectionGeneratorFactory.build(getASTMethod("onCreate"), astType));
        serviceDescriptor.getGenerators().add(scopesGenerationFactory.build(getASTMethod("onCreate")));

        serviceDescriptor.setAnalysisContext(context);

        serviceDescriptor.getGenerators().add(buildEventMethod(OnCreate.class, "onCreate"));

        serviceDescriptor.getGenerators().add(buildEventMethod(OnConfigurationChanged.class, "onConfigurationChanged", AndroidLiterals.CONTENT_CONFIGURATION));
        //onDestroy
        serviceDescriptor.getGenerators().add(buildEventMethod(OnDestroy.class, "onDestroy"));
        //onLowMemory
        serviceDescriptor.getGenerators().add(buildEventMethod(OnLowMemory.class, "onLowMemory"));
        //onRebind(android.content.Intent intent)
        serviceDescriptor.getGenerators().add(buildEventMethod(OnRebind.class, "onRebind", AndroidLiterals.INTENT));
        //onHandleIntent(android.content.Intent intent)
        serviceDescriptor.getGenerators().add(buildEventMethod(OnHandleIntent.class, AndroidLiterals.INTENT_SERVICE, "onHandleIntent", AndroidLiterals.INTENT));
        //onTaskRemoved(Intent rootIntent)
        //serviceDescriptor.addGenerators(buildEventMethod(OnTaskRemoved.class, "onTaskRemoved", Intent.class));

        serviceDescriptor.getGenerators().add(new OnBindGenerator());

        serviceDescriptor.getGenerators().add(listenerRegistrationGeneratorFactory.build(getASTMethod("onCreate")));

        //todo: serviceDescriptor.getPostInjectionGenerators().add(contextScopeComponentBuilder);

        serviceDescriptor.getGenerators().add(observesExpressionDecoratorFactory.build(
                getASTMethod("onCreate"),
                getASTMethod("onCreate"),
                getASTMethod("onDestroy")
        ));

        serviceDescriptor.getGenerators().add(generatorFactory.buildStrategyGenerator(ServiceIntentFactoryStrategy.class));
    }

    private org.androidtransfuse.experiment.generators.MethodCallbackGenerator buildEventMethod(Class<? extends Annotation> eventAnnotationClass, ASTType targetComponent, String methodName, ASTType... args) {
        ASTMethod method = getASTMethod(targetComponent, methodName, args);
        ASTType eventAnnotation = astClassFactory.getType(eventAnnotationClass);

        return componentBuilderFactory.buildMethodCallbackGenerator(eventAnnotation, method, getASTMethod("onCreate"));
    }

    private org.androidtransfuse.experiment.generators.MethodCallbackGenerator buildEventMethod(Class<? extends Annotation> eventAnnotationClass, String methodName, ASTType... args) {
        ASTMethod method = getASTMethod(methodName, args);
        ASTType eventAnnotation = astClassFactory.getType(eventAnnotationClass);

        return componentBuilderFactory.buildMethodCallbackGenerator(eventAnnotation, method, getASTMethod("onCreate"));
    }

    private ASTMethod getASTMethod(String methodName, ASTType... args) {
        return getASTMethod(AndroidLiterals.SERVICE, methodName, args);
    }

    private ASTMethod getASTMethod(ASTType type, String methodName, ASTType... args) {
        return astElementFactory.findMethod(type, methodName, args);
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(TypeMirror type) {

        InjectionNodeBuilderRepository injectionNodeBuilderRepository = injectionNodeRepositoryProvider.get();

        injectionNodeBuilderRepository.putType(AndroidLiterals.CONTEXT, injectionBindingBuilder.buildThis(AndroidLiterals.CONTEXT));
        injectionNodeBuilderRepository.putType(AndroidLiterals.APPLICATION, injectionBindingBuilder.dependency(AndroidLiterals.CONTEXT).invoke(AndroidLiterals.APPLICATION, "getApplication").build());
        injectionNodeBuilderRepository.putType(AndroidLiterals.SERVICE, injectionBindingBuilder.buildThis(AndroidLiterals.SERVICE));
        injectionNodeBuilderRepository.putType(ContextScopeHolder.class, injectionBindingBuilder.buildThis(ContextScopeHolder.class));

        if (type != null && !type.toString().equals(AndroidLiterals.SERVICE.getName())) {
            ASTType serviceASTType = type.accept(astTypeBuilderVisitor, null);
            injectionNodeBuilderRepository.putType(serviceASTType, injectionBindingBuilder.buildThis(serviceASTType));
        }

        if(type != null){
            ASTType serviceASTType = type.accept(astTypeBuilderVisitor, null);

            while(!serviceASTType.equals(AndroidLiterals.SERVICE) && serviceASTType.inheritsFrom(AndroidLiterals.SERVICE)){
                injectionNodeBuilderRepository.putType(serviceASTType, injectionBindingBuilder.buildThis(serviceASTType));
                serviceASTType = serviceASTType.getSuperClass();
            }
        }

        injectionNodeBuilderRepository.addRepository(injectionNodeBuilderRepositoryFactory.buildApplicationInjections());
        injectionNodeBuilderRepository.addRepository(injectionNodeBuilderRepositoryFactory.buildModuleConfiguration());

        return injectionNodeBuilderRepository;

    }

    private static class ServiceTypeMirrorRunnable extends TypeMirrorRunnable<Service> {
        public ServiceTypeMirrorRunnable(Service serviceAnnotation) {
            super(serviceAnnotation);
        }

        @Override
        public void run(Service annotation) {
            annotation.type();
        }
    }

    private final class OnBindGenerator implements Generation {
        @Override
        public void schedule(ComponentBuilder builder, ComponentDescriptor descriptor) {
            builder.add(getASTMethod("onBind", AndroidLiterals.INTENT), GenerationPhase.INIT, new ComponentMethodGenerator() {
                @Override
                public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                    block._return(JExpr._null());
                }
            });
        }
    }
}