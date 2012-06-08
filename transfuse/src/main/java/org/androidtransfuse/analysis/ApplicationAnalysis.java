package org.androidtransfuse.analysis;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.Application;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.MethodCallbackGenerator;
import org.androidtransfuse.gen.componentBuilder.NoOpLayoutBuilder;
import org.androidtransfuse.gen.componentBuilder.OnCreateComponentBuilder;
import org.androidtransfuse.gen.variableBuilder.ResourcesInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.processor.ManifestManager;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ApplicationAnalysis {

    private InjectionPointFactory injectionPointFactory;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider;
    private Provider<org.androidtransfuse.model.manifest.Application> applicationProvider;
    private InjectionNodeBuilderRepository injectionNodeBuilders;
    private ComponentBuilderFactory componentBuilderFactory;
    private ASTClassFactory astClassFactory;
    private AnalysisContextFactory analysisContextFactory;
    private RResource rResource;
    private ManifestManager manifestManager;

    @Inject
    public ApplicationAnalysis(InjectionPointFactory injectionPointFactory,
                               VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                               InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory,
                               Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider,
                               Provider<org.androidtransfuse.model.manifest.Application> applicationProvider,
                               InjectionNodeBuilderRepository injectionNodeBuilders,
                               ComponentBuilderFactory componentBuilderFactory,
                               ASTClassFactory astClassFactory,
                               AnalysisContextFactory analysisContextFactory, RResource rResource, ManifestManager manifestManager) {
        this.injectionPointFactory = injectionPointFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.resourcesInjectionNodeBuilderProvider = resourcesInjectionNodeBuilderProvider;
        this.applicationProvider = applicationProvider;
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.componentBuilderFactory = componentBuilderFactory;
        this.astClassFactory = astClassFactory;
        this.analysisContextFactory = analysisContextFactory;
        this.rResource = rResource;
        this.manifestManager = manifestManager;
    }

    public void emptyApplication() {
        setupManifest(android.app.Application.class.getName(), null);
    }

    public ComponentDescriptor analyzeApplication(ASTType astType) {
        Application applicationAnnotation = astType.getAnnotation(Application.class);

        PackageClass inputType = new PackageClass(astType.getName());
        PackageClass applicationClassName;

        if (StringUtils.isBlank(applicationAnnotation.name())) {
            applicationClassName = inputType.appendName("Application");
        } else {
            applicationClassName = inputType.replaceName(applicationAnnotation.name());
        }

        ComponentDescriptor applicationDescriptor = new ComponentDescriptor(android.app.Application.class.getName(), applicationClassName);

        //analyze delegate
        AnalysisContext analysisContext = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap());
        InjectionNode injectionNode = injectionPointFactory.buildInjectionNode(astType, analysisContext);

        //application generation profile
        setupApplicationProfile(applicationDescriptor, injectionNode);

        //add manifest elements
        setupManifest(applicationDescriptor.getPackageClass().getFullyQualifiedName(), applicationAnnotation.label());

        return applicationDescriptor;
    }

    private void setupApplicationProfile(ComponentDescriptor applicationDescriptor, InjectionNode injectionNode) {

        try {
            ASTMethod onCreateASTMethod = astClassFactory.buildASTClassMethod(android.app.Application.class.getDeclaredMethod("onCreate"));
            //onCreate
            OnCreateComponentBuilder onCreateComponentBuilder = componentBuilderFactory.buildOnCreateComponentBuilder(injectionNode, new NoOpLayoutBuilder(), onCreateASTMethod);
            //onLowMemory
            onCreateComponentBuilder.addMethodCallbackBuilder(buildEventMethod("onLowMemory"));
            //onTerminate
            onCreateComponentBuilder.addMethodCallbackBuilder(buildEventMethod("onTerminate"));
            //onConfigurationChanged
            ASTMethod onConfigurationChangedASTMethod = astClassFactory.buildASTClassMethod(android.app.Application.class.getDeclaredMethod("onConfigurationChanged", Configuration.class));
            onCreateComponentBuilder.addMethodCallbackBuilder(
                    componentBuilderFactory.buildMethodCallbackGenerator("onConfigurationChanged",
                            componentBuilderFactory.buildSimpleMethodGenerator(onConfigurationChangedASTMethod, true)));

            applicationDescriptor.getComponentBuilders().add(onCreateComponentBuilder);
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while trying to build event method", e);
        }
    }

    private MethodCallbackGenerator buildEventMethod(String name) throws NoSuchMethodException {
        return buildEventMethod(name, name);
    }

    private MethodCallbackGenerator buildEventMethod(String eventName, String methodName) throws NoSuchMethodException {
        ASTMethod method = astClassFactory.buildASTClassMethod(android.app.Application.class.getMethod(methodName));

        return componentBuilderFactory.buildMethodCallbackGenerator(eventName,
                componentBuilderFactory.buildSimpleMethodGenerator(method, true));
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap() {

        InjectionNodeBuilderRepository subRepository = variableBuilderRepositoryFactory.buildRepository(injectionNodeBuilders);

        subRepository.put(Context.class.getName(), variableInjectionBuilderFactory.buildContextVariableInjectionNodeBuilder(android.app.Application.class));
        subRepository.put(android.app.Application.class.getName(), variableInjectionBuilderFactory.buildContextVariableInjectionNodeBuilder(android.app.Application.class));
        subRepository.put(Resources.class.getName(), resourcesInjectionNodeBuilderProvider.get());

        return subRepository;

    }

    private void setupManifest(String name, String label) {

        org.androidtransfuse.model.manifest.Application manifestApplication = applicationProvider.get();

        manifestApplication.setName(name);
        manifestApplication.setLabel(StringUtils.isBlank(label) ? null : label);

        manifestManager.setApplication(manifestApplication);
    }
}
