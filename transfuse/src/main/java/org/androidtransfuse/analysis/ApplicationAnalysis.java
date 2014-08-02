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
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.experiment.ScopesGeneration;
import org.androidtransfuse.experiment.generators.ApplicationManifestEntryGenerator;
import org.androidtransfuse.experiment.generators.ObservesExpressionGenerator;
import org.androidtransfuse.experiment.generators.OnCreateInjectionGenerator;
import org.androidtransfuse.experiment.generators.SuperGenerator;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
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
public class ApplicationAnalysis implements Analysis<ComponentDescriptor> {

    private final InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private final Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final ASTClassFactory astClassFactory;
    private final ASTElementFactory astElementFactory;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final AnalysisContextFactory analysisContextFactory;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final ObservesExpressionGenerator.ObservesExpressionGeneratorFactory observesExpressionGeneratorFactory;
    private final SuperGenerator.SuperGeneratorFactory superGeneratorFactory;
    private final OnCreateInjectionGenerator.InjectionGeneratorFactory injectionGeneratorFactory;
    private final ApplicationManifestEntryGenerator applicationManifestEntryGenerator;
    private final ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory;

    @Inject
    public ApplicationAnalysis(InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory,
                               Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider,
                               ComponentBuilderFactory componentBuilderFactory,
                               ASTClassFactory astClassFactory,
                               ASTElementFactory astElementFactory,
                               ASTTypeBuilderVisitor astTypeBuilderVisitor,
                               AnalysisContextFactory analysisContextFactory,
                               InjectionBindingBuilder injectionBindingBuilder,
                               ObservesExpressionGenerator.ObservesExpressionGeneratorFactory observesExpressionGeneratorFactory,
                               SuperGenerator.SuperGeneratorFactory superGeneratorFactory,
                               OnCreateInjectionGenerator.InjectionGeneratorFactory injectionGeneratorFactory,
                               ApplicationManifestEntryGenerator applicationManifestEntryGenerator, ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory) {
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.injectionNodeBuilderRepositoryProvider = injectionNodeBuilderRepositoryProvider;
        this.componentBuilderFactory = componentBuilderFactory;
        this.astClassFactory = astClassFactory;
        this.astElementFactory = astElementFactory;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.analysisContextFactory = analysisContextFactory;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.observesExpressionGeneratorFactory = observesExpressionGeneratorFactory;
        this.superGeneratorFactory = superGeneratorFactory;
        this.injectionGeneratorFactory = injectionGeneratorFactory;
        this.applicationManifestEntryGenerator = applicationManifestEntryGenerator;
        this.scopesGenerationFactory = scopesGenerationFactory;
    }

    public ComponentDescriptor analyze(ASTType astType) {
        Application applicationAnnotation = astType.getAnnotation(Application.class);


        PackageClass applicationClassName;
        ComponentDescriptor applicationDescriptor = null;

        if (astType.extendsFrom(AndroidLiterals.APPLICATION)) {
            //vanilla Android Application
            PackageClass activityPackageClass = astType.getPackageClass();
            applicationClassName = buildPackageClass(astType, activityPackageClass.getClassName());
            applicationDescriptor = new ComponentDescriptor(astType, null, applicationClassName);
        } else {

            applicationClassName = buildPackageClass(astType, applicationAnnotation.name());

            TypeMirror type = getTypeMirror(new ApplicationTypeMirrorRunnable(applicationAnnotation));

            ASTType applicationType = type == null || type.toString().equals("java.lang.Object") ? AndroidLiterals.APPLICATION : type.accept(astTypeBuilderVisitor, null);

            applicationDescriptor = new ComponentDescriptor(astType, applicationType, applicationClassName);

            //analyze delegate
            AnalysisContext analysisContext = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(type));

            //application generation profile
            setupApplicationProfile(applicationDescriptor, astType, analysisContext);
        }

        //add manifest elements
        applicationDescriptor.getGenerators().add(applicationManifestEntryGenerator);

        return applicationDescriptor;
    }

    private PackageClass buildPackageClass(ASTType input, String applicationNAme) {

        PackageClass inputPackageClass = input.getPackageClass();

        if (StringUtils.isBlank(applicationNAme)) {
            return inputPackageClass.append("Application");
        } else {
            return inputPackageClass.replaceName(applicationNAme);
        }
    }

    private void setupApplicationProfile(ComponentDescriptor applicationDescriptor, ASTType astType, AnalysisContext context) {

        //onCreate
        //applicationDescriptor.setInitMethodBuilder(astClassFactory.getType(OnCreate.class), componentBuilderFactory.buildOnCreateMethodBuilder(onCreateASTMethod, new NoOpWindowFeatureBuilder(), new NoOpLayoutBuilder()));
        applicationDescriptor.getGenerators().add(superGeneratorFactory.build(getASTMethod("onCreate")));
        applicationDescriptor.getGenerators().add(scopesGenerationFactory.build(getASTMethod("onCreate")));

        applicationDescriptor.getGenerateFirst().add(new MethodSignature(getASTMethod("onCreate")));
        applicationDescriptor.getGenerators().add(injectionGeneratorFactory.build(getASTMethod("onCreate"), astType));

        //applicationDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildInjectionNodeFactory(ImmutableSet.<ASTAnnotation>of(), astType, context));
        applicationDescriptor.setAnalysisContext(context);

        applicationDescriptor.getGenerators().add(buildEventMethod(OnCreate.class, "onCreate"));
        //onLowMemory
        applicationDescriptor.getGenerators().add(buildEventMethod(OnLowMemory.class, "onLowMemory"));
        //onTerminate
        applicationDescriptor.getGenerators().add(buildEventMethod(OnTerminate.class, "onTerminate"));
        //onConfigurationChanged
        ASTMethod onConfigurationChangedASTMethod = getASTMethod("onConfigurationChanged", AndroidLiterals.CONTENT_CONFIGURATION);
        applicationDescriptor.getGenerators().add(
                componentBuilderFactory.buildMethodCallbackGenerator(astClassFactory.getType(OnConfigurationChanged.class), onConfigurationChangedASTMethod, getASTMethod("onCreate"))
        );

        applicationDescriptor.getGenerators().add(observesExpressionGeneratorFactory.build(
                getASTMethod("onCreate"),
                getASTMethod("onCreate"),
                getASTMethod("onTerminate")
        ));
    }

    private org.androidtransfuse.experiment.generators.MethodCallbackGenerator buildEventMethod(Class<? extends Annotation> eventAnnotationClass, String methodName) {
        ASTMethod method = getASTMethod(methodName);
        ASTType eventAnnotation = astClassFactory.getType(eventAnnotationClass);

        return componentBuilderFactory.buildMethodCallbackGenerator(eventAnnotation, method, getASTMethod("onCreate"));
    }

    private ASTMethod getASTMethod(String methodName, ASTType... args) {
        return astElementFactory.findMethod(AndroidLiterals.APPLICATION, methodName, args);
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(TypeMirror applicationType) {
        InjectionNodeBuilderRepository injectionNodeBuilderRepository = injectionNodeBuilderRepositoryProvider.get();

        injectionNodeBuilderRepository.putType(AndroidLiterals.CONTEXT, injectionBindingBuilder.buildThis(AndroidLiterals.CONTEXT));
        injectionNodeBuilderRepository.putType(AndroidLiterals.APPLICATION, injectionBindingBuilder.buildThis((AndroidLiterals.APPLICATION)));

        if(applicationType != null){
            ASTType applicationASTType = applicationType.accept(astTypeBuilderVisitor, null);

            while(!applicationASTType.equals(AndroidLiterals.APPLICATION) && applicationASTType.inheritsFrom(AndroidLiterals.APPLICATION)){
                injectionNodeBuilderRepository.putType(applicationASTType, injectionBindingBuilder.buildThis(applicationASTType));
                applicationASTType = applicationASTType.getSuperClass();
            }
        }


        injectionNodeBuilderRepository.addRepository(variableBuilderRepositoryFactory.buildApplicationInjections());
        injectionNodeBuilderRepository.addRepository(variableBuilderRepositoryFactory.buildModuleConfiguration());

        return injectionNodeBuilderRepository;

    }

    private static class ApplicationTypeMirrorRunnable extends TypeMirrorRunnable<Application> {
        public ApplicationTypeMirrorRunnable(Application ApplicationAnnotation) {
            super(ApplicationAnnotation);
        }

        @Override
        public void run(Application annotation) {
            annotation.type();
        }
    }
}
