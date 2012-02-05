package org.androidtransfuse.analysis;

import android.content.Context;
import android.content.res.Resources;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Application;
import org.androidtransfuse.gen.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.VariableBuilderRepositoryFactory;
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
    private VariableBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider;

    @Inject
    public ApplicationAnalysis(InjectionPointFactory injectionPointFactory,
                               Provider<ContextVariableInjectionNodeBuilder> contextVariableBuilderProvider,
                               VariableBuilderRepositoryFactory variableBuilderRepositoryFactory,
                               Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider) {
        this.injectionPointFactory = injectionPointFactory;
        this.contextVariableBuilderProvider = contextVariableBuilderProvider;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.resourcesInjectionNodeBuilderProvider = resourcesInjectionNodeBuilderProvider;
    }

    public ApplicationDescriptor analyzeApplication(ASTType astType, AnalysisRepository analysisRepository, InjectionNodeBuilderRepository injectionNodeBuilders, AOPRepository aopRepository) {
        ApplicationDescriptor applicationDescriptor = null;

        Application activityAnnotation = astType.getAnnotation(Application.class);

        if (activityAnnotation != null) {
            applicationDescriptor = new ApplicationDescriptor();

            applicationDescriptor.setLabel(activityAnnotation.name());

            String name = astType.getName();
            String packageName = name.substring(0, name.lastIndexOf('.'));

            applicationDescriptor.setPackageClass(new PackageClass(packageName, activityAnnotation.name()));

            AnalysisContext context = new AnalysisContext(analysisRepository, buildVariableBuilderMap(injectionNodeBuilders), aopRepository);

            applicationDescriptor.addInjectionNode(
                    injectionPointFactory.buildInjectionNode(astType, context));


            org.androidtransfuse.model.manifest.Application manifestApplication = new org.androidtransfuse.model.manifest.Application();

            manifestApplication.setName("." + activityAnnotation.name());
            manifestApplication.setMergeTag("yes");//todo: common tagger?

            applicationDescriptor.setManifestApplication(manifestApplication);
        }

        return applicationDescriptor;
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(InjectionNodeBuilderRepository injectionNodeBuilderRepository) {

        InjectionNodeBuilderRepository subRepository = variableBuilderRepositoryFactory.buildRepository(injectionNodeBuilderRepository);

        subRepository.put(Context.class.getName(), contextVariableBuilderProvider.get());
        subRepository.put(android.app.Application.class.getName(), contextVariableBuilderProvider.get());
        subRepository.put(Resources.class.getName(), resourcesInjectionNodeBuilderProvider.get());

        return subRepository;

    }
}
