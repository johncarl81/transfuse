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
import org.androidtransfuse.gen.variableBuilder.ProviderInjectionNodeBuilderFactory;
import org.androidtransfuse.intentFactory.ServiceIntentFactoryStrategy;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.scope.ApplicationScope;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;
import javax.lang.model.type.TypeMirror;

import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * Service related Analysis
 *
 * @author John Ericksen
 */
public class ServiceAnalysis implements Analysis<ComponentDescriptor> {

    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory;
    private final AnalysisContextFactory analysisContextFactory;
    private final ASTElementFactory astElementFactory;
    private final ProviderInjectionNodeBuilderFactory providerInjectionNodeBuilder;
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
    public ServiceAnalysis(InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory,
                           AnalysisContextFactory analysisContextFactory,
                           ASTElementFactory astElementFactory,
                           ProviderInjectionNodeBuilderFactory providerInjectionNodeBuilder,
                           InjectionBindingBuilder injectionBindingBuilder,
                           ASTTypeBuilderVisitor astTypeBuilderVisitor,
                           GeneratorFactory generatorFactory,
                           ListenerRegistrationGenerator.ListerRegistrationGeneratorFactory listenerRegistrationGeneratorFactory,
                           ObservesExpressionGenerator.ObservesExpressionGeneratorFactory observesExpressionDecoratorFactory,
                           ServiceManifestEntryGenerator serviceManifestEntryGenerator,
                           OnCreateInjectionGenerator.InjectionGeneratorFactory onCreateInjectionGeneratorFactory,
                           ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory,
                           ComponentAnalysis componentAnalysis) {
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
        this.analysisContextFactory = analysisContextFactory;
        this.astElementFactory = astElementFactory;
        this.providerInjectionNodeBuilder = providerInjectionNodeBuilder;
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
            serviceClassName = componentAnalysis.buildComponentPackageClass(input, packageClass.getClassName(), "Service");

            serviceDescriptor = new ComponentDescriptor(input, null, packageClass);
        } else {
            //generated Android Service
            serviceClassName = componentAnalysis.buildComponentPackageClass(input, serviceAnnotation.name(), "Service");

            TypeMirror type = getTypeMirror(serviceAnnotation, "type");

            ASTType serviceType = type == null || type.toString().equals("java.lang.Object") ? AndroidLiterals.SERVICE : type.accept(astTypeBuilderVisitor, null);

            AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(serviceType));

            serviceDescriptor = new ComponentDescriptor(input, serviceType, serviceClassName, context);

            componentAnalysis.setupGenerators(serviceDescriptor, serviceType, Service.class);

            serviceDescriptor.getGenerators().add(onCreateInjectionGeneratorFactory.build(getASTMethod("onCreate"), input));
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

        serviceDescriptor.getGenerators().add(serviceManifestEntryGenerator);

        return serviceDescriptor;
    }

    private ASTMethod getASTMethod(String methodName, ASTType... args) {
        return getASTMethod(AndroidLiterals.SERVICE, methodName, args);
    }

    private ASTMethod getASTMethod(ASTType type, String methodName, ASTType... args) {
        return astElementFactory.findMethod(type, methodName, args);
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(ASTType serviceType) {

        InjectionNodeBuilderRepository injectionNodeBuilderRepository = componentAnalysis.setupInjectionNodeBuilderRepository(serviceType, Service.class);


        ASTType applicationScopeType = astElementFactory.getType(ApplicationScope.ApplicationScopeQualifier.class);
        ASTType applicationProvider = astElementFactory.getType(ApplicationScope.ApplicationProvider.class);
        injectionNodeBuilderRepository.putType(AndroidLiterals.APPLICATION, providerInjectionNodeBuilder.builderProviderBuilder(applicationProvider));
        injectionNodeBuilderRepository.putType(AndroidLiterals.CONTEXT, injectionBindingBuilder.buildThis(AndroidLiterals.CONTEXT));
        injectionNodeBuilderRepository.putScoped(new InjectionSignature(AndroidLiterals.APPLICATION), applicationScopeType);

        injectionNodeBuilderRepository.putType(AndroidLiterals.SERVICE, injectionBindingBuilder.buildThis(AndroidLiterals.SERVICE));

        while(!serviceType.equals(AndroidLiterals.SERVICE) && serviceType.inheritsFrom(AndroidLiterals.SERVICE)){
            injectionNodeBuilderRepository.putType(serviceType, injectionBindingBuilder.buildThis(serviceType));
            serviceType = serviceType.getSuperClass();
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