package org.androidtransfuse.analysis;

import android.content.Context;
import android.content.res.Resources;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Application;
import org.androidtransfuse.gen.AndroidComponentDescriptor;
import org.androidtransfuse.gen.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.OnCreateComponentBuilder;
import org.androidtransfuse.gen.componentBuilder.ScopingComponentBuilder;
import org.androidtransfuse.gen.variableBuilder.ContextVariableInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.ResourcesInjectionNodeBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.processor.ProcessorContext;
import org.apache.commons.lang.StringUtils;

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
    private Provider<ScopingComponentBuilder> scopingComponentBuilderProvider;
    private ComponentBuilderFactory componentBuilderFactory;

    @Inject
    public ApplicationAnalysis(InjectionPointFactory injectionPointFactory,
                               Provider<ContextVariableInjectionNodeBuilder> contextVariableBuilderProvider,
                               InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory,
                               Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider,
                               Provider<org.androidtransfuse.model.manifest.Application> applicationProvider,
                               AOPRepository aopRepository,
                               InjectionNodeBuilderRepository injectionNodeBuilders,
                               Provider<ScopingComponentBuilder> scopingComponentBuilderProvider,
                               Provider<OnCreateComponentBuilder> onCreateComponentBuilderProvider,
                               ComponentBuilderFactory componentBuilderFactory) {
        this.injectionPointFactory = injectionPointFactory;
        this.contextVariableBuilderProvider = contextVariableBuilderProvider;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.resourcesInjectionNodeBuilderProvider = resourcesInjectionNodeBuilderProvider;
        this.applicationProvider = applicationProvider;
        this.aopRepository = aopRepository;
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.scopingComponentBuilderProvider = scopingComponentBuilderProvider;
        this.componentBuilderFactory = componentBuilderFactory;
    }

    public AndroidComponentDescriptor emptyApplication(ProcessorContext context) {

        String packageName = context.getManifest().getApplicationPackage();

        PackageClass applicationClassName = new PackageClass(packageName, "TransfuseApplication");

        AndroidComponentDescriptor applicationDescriptor = new AndroidComponentDescriptor(android.app.Application.class.getName(), applicationClassName);

        setupManifest(context, ".TransfuseApplication", null);

        return applicationDescriptor;
    }

    public AndroidComponentDescriptor analyzeApplication(ProcessorContext context, ASTType astType, AnalysisRepository analysisRepository) {
        Application applicationAnnotation = astType.getAnnotation(Application.class);

        String name = astType.getName();
        String packageName = name.substring(0, name.lastIndexOf('.'));
        String deleagateName = name.substring(name.lastIndexOf('.') + 1);
        PackageClass applicationClassName;

        if (StringUtils.isBlank(applicationAnnotation.name())) {
            applicationClassName = new PackageClass(packageName, deleagateName + "Application");
        } else {
            applicationClassName = new PackageClass(packageName, applicationAnnotation.name());
        }

        AndroidComponentDescriptor applicationDescriptor = new AndroidComponentDescriptor(android.app.Application.class.getName(), applicationClassName);

        //analyze delegate
        AnalysisContext analysisContext = new AnalysisContext(analysisRepository, buildVariableBuilderMap(), aopRepository);
        InjectionNode injectionNode = injectionPointFactory.buildInjectionNode(astType, analysisContext);

        //application generation profile
        setupApplicationProfile(applicationDescriptor, injectionNode);

        //add manifest elements
        setupManifest(context, applicationDescriptor.getPackageClass().getFullyQualifiedName(), applicationAnnotation.label());

        return applicationDescriptor;
    }

    private void setupApplicationProfile(AndroidComponentDescriptor applicationDescriptor, InjectionNode injectionNode) {

        //onCreate
        OnCreateComponentBuilder onCreateComponentBuilder = componentBuilderFactory.buildOnCreateComponentBuilder(injectionNode);
        //onLowMemory
        onCreateComponentBuilder.addMethodCallbackBuilder(componentBuilderFactory.buildMethodCallbackGenerator("onLowMemory"));
        //onTerminate
        onCreateComponentBuilder.addMethodCallbackBuilder(componentBuilderFactory.buildMethodCallbackGenerator("onTerminate"));
        //onConfigurationChanged
        //todo:onCreateComponentBuilder.addMethodCallbackBuilder(componentBuilderFactory.buildMethodCallbackGenerator("onConfigurationChanged", Configuration.class));

        //scoping
        applicationDescriptor.getComponentBuilders().add(scopingComponentBuilderProvider.get());

        applicationDescriptor.getComponentBuilders().add(onCreateComponentBuilder);
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap() {

        InjectionNodeBuilderRepository subRepository = variableBuilderRepositoryFactory.buildRepository(injectionNodeBuilders);

        subRepository.put(Context.class.getName(), contextVariableBuilderProvider.get());
        subRepository.put(android.app.Application.class.getName(), contextVariableBuilderProvider.get());
        subRepository.put(Resources.class.getName(), resourcesInjectionNodeBuilderProvider.get());

        return subRepository;

    }

    private void setupManifest(ProcessorContext context, String name, String label) {

        org.androidtransfuse.model.manifest.Application manifestApplication = applicationProvider.get();

        manifestApplication.setName(name);
        manifestApplication.setLabel(label);

        context.getSourceManifest().getApplications().add(manifestApplication);
    }
}
