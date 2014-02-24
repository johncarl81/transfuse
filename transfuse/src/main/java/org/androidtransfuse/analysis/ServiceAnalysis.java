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
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.Service;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.experiment.generators.ObservesExpressionGenerator;
import org.androidtransfuse.experiment.generators.OnCreateInjectionGenerator;
import org.androidtransfuse.experiment.generators.ServiceManifestEntryGenerator;
import org.androidtransfuse.gen.GeneratorFactory;
import org.androidtransfuse.gen.componentBuilder.ListenerRegistrationGenerator;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.intentFactory.ServiceIntentFactoryStrategy;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.util.AndroidLiterals;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;

import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * Service related Analysis
 *
 * @author John Ericksen
 */
public class ServiceAnalysis implements Analysis<ComponentDescriptor> {

    private final Provider<InjectionNodeBuilderRepository> injectionNodeRepositoryProvider;
    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory;
    private final AnalysisContextFactory analysisContextFactory;
    private final ASTElementFactory astElementFactory;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final GeneratorFactory generatorFactory;
    private final ListenerRegistrationGenerator.ListerRegistrationGeneratorFactory listenerRegistrationGeneratorFactory;
    private final ObservesExpressionGenerator.ObservesExpressionGeneratorFactory observesExpressionDecoratorFactory;
    private final ServiceManifestEntryGenerator serviceManifestEntryGenerator;
    private final OnCreateInjectionGenerator.InjectionGeneratorFactory onCreateInjectionGeneratorFactory;
    private final ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory;
    private final ComponentAnalysis componentAnalysis;

    @Inject
    public ServiceAnalysis(Provider<InjectionNodeBuilderRepository> injectionNodeRepositoryProvider,
                           InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory,
                           AnalysisContextFactory analysisContextFactory,
                           ASTElementFactory astElementFactory,
                           InjectionBindingBuilder injectionBindingBuilder,
                           ASTTypeBuilderVisitor astTypeBuilderVisitor,
                           GeneratorFactory generatorFactory,
                           ListenerRegistrationGenerator.ListerRegistrationGeneratorFactory listenerRegistrationGeneratorFactory,
                           ObservesExpressionGenerator.ObservesExpressionGeneratorFactory observesExpressionDecoratorFactory,
                           ServiceManifestEntryGenerator serviceManifestEntryGenerator,
                           OnCreateInjectionGenerator.InjectionGeneratorFactory onCreateInjectionGeneratorFactory,
                           ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory,
                           ComponentAnalysis componentAnalysis) {
        this.injectionNodeRepositoryProvider = injectionNodeRepositoryProvider;
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
        this.analysisContextFactory = analysisContextFactory;
        this.astElementFactory = astElementFactory;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.generatorFactory = generatorFactory;
        this.listenerRegistrationGeneratorFactory = listenerRegistrationGeneratorFactory;
        this.observesExpressionDecoratorFactory = observesExpressionDecoratorFactory;
        this.serviceManifestEntryGenerator = serviceManifestEntryGenerator;
        this.onCreateInjectionGeneratorFactory = onCreateInjectionGeneratorFactory;
        this.scopesGenerationFactory = scopesGenerationFactory;
        this.componentAnalysis = componentAnalysis;
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

            TypeMirror type = getTypeMirror(serviceAnnotation, "type");

            ASTType serviceType = type == null || type.toString().equals("java.lang.Object") ? AndroidLiterals.SERVICE : type.accept(astTypeBuilderVisitor, null);

            AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(type));

            serviceDescriptor = new ComponentDescriptor(input, serviceType, serviceClassName, context);

            componentAnalysis.setupGenerators(serviceDescriptor, serviceType, Service.class);

            //application generation profile
            setupServiceProfile(serviceDescriptor, input);
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

    private void setupServiceProfile(ComponentDescriptor serviceDescriptor, ASTType astType) {

        serviceDescriptor.getGenerators().add(onCreateInjectionGeneratorFactory.build(getASTMethod("onCreate"), astType));
        serviceDescriptor.getGenerators().add(scopesGenerationFactory.build(getASTMethod("onCreate")));

        serviceDescriptor.getGenerators().add(new OnBindGenerator());

        serviceDescriptor.getGenerators().add(listenerRegistrationGeneratorFactory.build(getASTMethod("onCreate")));

        serviceDescriptor.getGenerators().add(observesExpressionDecoratorFactory.build(
                getASTMethod("onCreate"),
                getASTMethod("onCreate"),
                getASTMethod("onDestroy")
        ));

        serviceDescriptor.getGenerators().add(generatorFactory.buildStrategyGenerator(ServiceIntentFactoryStrategy.class));
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