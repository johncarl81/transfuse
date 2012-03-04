package org.androidtransfuse.analysis;

import android.content.Context;
import android.content.res.Resources;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Application;
import org.androidtransfuse.gen.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.variableBuilder.ContextVariableInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.ResourcesInjectionNodeBuilder;
import org.androidtransfuse.model.ApplicationDescriptor;
import org.androidtransfuse.model.PackageClass;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ApplicationAnalysis {

    private InjectionPointFactory injectionPointFactory;
    private Provider<ContextVariableInjectionNodeBuilder> contextVariableBuilderProvider;
    private InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider;
    private Provider<org.androidtransfuse.model.manifest.Application> applicationProvider;
    private InjectionNodeBuilderRepository injectionNodeBuilders;
    private AOPRepository aopRepository;

    @Inject
    public ApplicationAnalysis(InjectionPointFactory injectionPointFactory,
                               Provider<ContextVariableInjectionNodeBuilder> contextVariableBuilderProvider,
                               InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory,
                               Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider,
                               Provider<org.androidtransfuse.model.manifest.Application> applicationProvider,
                               AOPRepository aopRepository,
                               InjectionNodeBuilderRepository injectionNodeBuilders) {
        this.injectionPointFactory = injectionPointFactory;
        this.contextVariableBuilderProvider = contextVariableBuilderProvider;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.resourcesInjectionNodeBuilderProvider = resourcesInjectionNodeBuilderProvider;
        this.applicationProvider = applicationProvider;
        this.aopRepository = aopRepository;
        this.injectionNodeBuilders = injectionNodeBuilders;
    }

    public ApplicationDescriptor emptyApplication(String packageName) {
        ApplicationDescriptor applicationDescriptor = new ApplicationDescriptor();

        applicationDescriptor.setLabel("Android Application");

        applicationDescriptor.setPackageClass(new PackageClass(packageName, "TransfuseApplication"));

        org.androidtransfuse.model.manifest.Application manifestApplication = applicationProvider.get();

        manifestApplication.setName(".TransfuseApplication");

        applicationDescriptor.setManifestApplication(manifestApplication);

        return applicationDescriptor;
    }

    public ApplicationDescriptor analyzeApplication(ASTType astType, AnalysisRepository analysisRepository) {
        ApplicationDescriptor applicationDescriptor = null;

        Application activityAnnotation = astType.getAnnotation(Application.class);

        if (activityAnnotation != null) {
            applicationDescriptor = new ApplicationDescriptor();

            applicationDescriptor.setLabel(activityAnnotation.name());

            String name = astType.getName();
            String packageName = name.substring(0, name.lastIndexOf('.'));

            applicationDescriptor.setPackageClass(new PackageClass(packageName, activityAnnotation.name()));

            AnalysisContext context = new AnalysisContext(analysisRepository, buildVariableBuilderMap(), aopRepository);

            applicationDescriptor.addInjectionNode(
                    injectionPointFactory.buildInjectionNode(astType, context));


            org.androidtransfuse.model.manifest.Application manifestApplication = applicationProvider.get();

            manifestApplication.setName("." + activityAnnotation.name());

            applicationDescriptor.setManifestApplication(manifestApplication);
        }

        return applicationDescriptor;
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap() {

        InjectionNodeBuilderRepository subRepository = variableBuilderRepositoryFactory.buildRepository(injectionNodeBuilders);

        subRepository.put(Context.class.getName(), contextVariableBuilderProvider.get());
        subRepository.put(android.app.Application.class.getName(), contextVariableBuilderProvider.get());
        subRepository.put(Resources.class.getName(), resourcesInjectionNodeBuilderProvider.get());

        return subRepository;

    }
}
