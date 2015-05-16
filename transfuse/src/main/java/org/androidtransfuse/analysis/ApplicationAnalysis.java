/**
 * Copyright 2011-2015 John Ericksen
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
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.Application;
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.experiment.ScopesGeneration;
import org.androidtransfuse.experiment.generators.ApplicationManifestEntryGenerator;
import org.androidtransfuse.experiment.generators.ApplicationScopeSeedGenerator;
import org.androidtransfuse.experiment.generators.ObservesExpressionGenerator;
import org.androidtransfuse.experiment.generators.OnCreateInjectionGenerator;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;
import javax.lang.model.type.TypeMirror;

import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * @author John Ericksen
 */
public class ApplicationAnalysis implements Analysis<ComponentDescriptor> {

    private final InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private final ASTElementFactory astElementFactory;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final AnalysisContextFactory analysisContextFactory;
    private final ObservesExpressionGenerator.ObservesExpressionGeneratorFactory observesExpressionGeneratorFactory;
    private final OnCreateInjectionGenerator.InjectionGeneratorFactory injectionGeneratorFactory;
    private final ApplicationManifestEntryGenerator applicationManifestEntryGenerator;
    private final ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory;
    private final ComponentAnalysis componentAnalysis;
    private final ApplicationScopeSeedGenerator applicationScopeSeedGenerator;

    @Inject
    public ApplicationAnalysis(InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory,
                               ASTElementFactory astElementFactory,
                               ASTTypeBuilderVisitor astTypeBuilderVisitor,
                               AnalysisContextFactory analysisContextFactory,
                               ObservesExpressionGenerator.ObservesExpressionGeneratorFactory observesExpressionGeneratorFactory,
                               OnCreateInjectionGenerator.InjectionGeneratorFactory injectionGeneratorFactory,
                               ApplicationManifestEntryGenerator applicationManifestEntryGenerator,
                               ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory,
                               ComponentAnalysis componentAnalysis,
                               ApplicationScopeSeedGenerator applicationScopeSeedGenerator) {
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.astElementFactory = astElementFactory;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.analysisContextFactory = analysisContextFactory;
        this.observesExpressionGeneratorFactory = observesExpressionGeneratorFactory;
        this.injectionGeneratorFactory = injectionGeneratorFactory;
        this.applicationManifestEntryGenerator = applicationManifestEntryGenerator;
        this.scopesGenerationFactory = scopesGenerationFactory;
        this.componentAnalysis = componentAnalysis;
        this.applicationScopeSeedGenerator = applicationScopeSeedGenerator;
    }

    public ComponentDescriptor analyze(ASTType astType) {
        Application applicationAnnotation = astType.getAnnotation(Application.class);


        PackageClass applicationClassName;
        ComponentDescriptor applicationDescriptor = null;

        if (astType.extendsFrom(AndroidLiterals.APPLICATION)) {
            //vanilla Android Application
            PackageClass activityPackageClass = astType.getPackageClass();
            applicationClassName = componentAnalysis.buildComponentPackageClass(astType, activityPackageClass.getClassName(), "Application");
            applicationDescriptor = new ComponentDescriptor(astType, null, applicationClassName);
        } else {

            applicationClassName = componentAnalysis.buildComponentPackageClass(astType, applicationAnnotation.name(), "Application");

            TypeMirror type = getTypeMirror(applicationAnnotation, "type");

            ASTType applicationType = type == null || type.toString().equals("java.lang.Object") ? AndroidLiterals.APPLICATION : type.accept(astTypeBuilderVisitor, null);

            //analyze delegate
            AnalysisContext analysisContext = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(applicationType));

            applicationDescriptor = new ComponentDescriptor(astType, applicationType, applicationClassName, analysisContext);

            componentAnalysis.setupGenerators(applicationDescriptor, applicationType, Application.class);

            applicationDescriptor.getGenerators().add(scopesGenerationFactory.build(getASTMethod("onCreate")));
            applicationDescriptor.getGenerators().add(injectionGeneratorFactory.build(getASTMethod("onCreate"), astType));
            applicationDescriptor.getGenerators().add(observesExpressionGeneratorFactory.build(
                    getASTMethod("onCreate"),
                    getASTMethod("onTerminate"),
                    getASTMethod("onCreate"),
                    getASTMethod("onTerminate")
            ));
            applicationDescriptor.getGenerators().add(applicationScopeSeedGenerator);
        }

        //add manifest elements
        applicationDescriptor.getGenerators().add(applicationManifestEntryGenerator);

        return applicationDescriptor;
    }

    private ASTMethod getASTMethod(String methodName, ASTType... args) {
        return astElementFactory.findMethod(AndroidLiterals.APPLICATION, methodName, args);
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(ASTType applicationType) {
        InjectionNodeBuilderRepository injectionNodeBuilderRepository = componentAnalysis.setupInjectionNodeBuilderRepository(applicationType, Application.class);

        injectionNodeBuilderRepository.addRepository(variableBuilderRepositoryFactory.buildModuleConfiguration());

        return injectionNodeBuilderRepository;

    }
}
