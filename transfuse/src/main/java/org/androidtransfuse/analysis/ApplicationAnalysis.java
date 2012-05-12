package org.androidtransfuse.analysis;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Application;
import org.androidtransfuse.gen.ComponentDescriptor;
import org.androidtransfuse.gen.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.componentBuilder.*;
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
    private Provider<ScopingComponentBuilder> scopingComponentBuilderProvider;
    private ComponentBuilderFactory componentBuilderFactory;
    private ASTClassFactory astClassFactory;
    private AnalysisContextFactory analysisContextFactory;

    @Inject
    public ApplicationAnalysis(InjectionPointFactory injectionPointFactory,
                               Provider<ContextVariableInjectionNodeBuilder> contextVariableBuilderProvider,
                               InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory,
                               Provider<ResourcesInjectionNodeBuilder> resourcesInjectionNodeBuilderProvider,
                               Provider<org.androidtransfuse.model.manifest.Application> applicationProvider,
                               InjectionNodeBuilderRepository injectionNodeBuilders,
                               Provider<ScopingComponentBuilder> scopingComponentBuilderProvider,
                               ComponentBuilderFactory componentBuilderFactory, ASTClassFactory astClassFactory, AnalysisContextFactory analysisContextFactory) {
        this.injectionPointFactory = injectionPointFactory;
        this.contextVariableBuilderProvider = contextVariableBuilderProvider;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.resourcesInjectionNodeBuilderProvider = resourcesInjectionNodeBuilderProvider;
        this.applicationProvider = applicationProvider;
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.scopingComponentBuilderProvider = scopingComponentBuilderProvider;
        this.componentBuilderFactory = componentBuilderFactory;
        this.astClassFactory = astClassFactory;
        this.analysisContextFactory = analysisContextFactory;
    }

    public ComponentDescriptor emptyApplication(ProcessorContext context) {

        String packageName = context.getManifest().getApplicationPackage();

        PackageClass applicationClassName = new PackageClass(packageName, "TransfuseApplication");

        ComponentDescriptor applicationDescriptor = new ComponentDescriptor(android.app.Application.class.getName(), applicationClassName);

        //application generation profile
        setupApplicationProfile(applicationDescriptor, null, context);

        setupManifest(context, ".TransfuseApplication", null);

        return applicationDescriptor;
    }

    public ComponentDescriptor analyzeApplication(ProcessorContext context, ASTType astType, AnalysisRepository analysisRepository) {
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

        ComponentDescriptor applicationDescriptor = new ComponentDescriptor(android.app.Application.class.getName(), applicationClassName);

        //analyze delegate
        AnalysisContext analysisContext = analysisContextFactory.buildAnalysisContext(analysisRepository, buildVariableBuilderMap());
        InjectionNode injectionNode = injectionPointFactory.buildInjectionNode(astType, analysisContext);

        //application generation profile
        setupApplicationProfile(applicationDescriptor, injectionNode, context);

        //add manifest elements
        setupManifest(context, applicationDescriptor.getPackageClass().getFullyQualifiedName(), applicationAnnotation.label());

        return applicationDescriptor;
    }

    private void setupApplicationProfile(ComponentDescriptor applicationDescriptor, InjectionNode injectionNode, ProcessorContext context) {

        try {
            ASTMethod onCreateASTMethod = astClassFactory.buildASTClassMethod(android.app.Application.class.getDeclaredMethod("onCreate"));
            //onCreate
            OnCreateComponentBuilder onCreateComponentBuilder = componentBuilderFactory.buildOnCreateComponentBuilder(injectionNode, new NoOpLayoutBuilder(), onCreateASTMethod, context.getRResource());
            //onLowMemory
            onCreateComponentBuilder.addMethodCallbackBuilder(buildEventMethod("onLowMemory"));
            //onTerminate
            onCreateComponentBuilder.addMethodCallbackBuilder(buildEventMethod("onTerminate"));
            //onConfigurationChanged
            ASTMethod onConfigurationChangedASTMethod = astClassFactory.buildASTClassMethod(android.app.Application.class.getDeclaredMethod("onConfigurationChanged", Configuration.class));
            onCreateComponentBuilder.addMethodCallbackBuilder(
                    componentBuilderFactory.buildMethodCallbackGenerator("onConfigurationChanged",
                            componentBuilderFactory.buildSimpleMethodGenerator(onConfigurationChangedASTMethod, true)));

            //scoping
            applicationDescriptor.getComponentBuilders().add(scopingComponentBuilderProvider.get());

            applicationDescriptor.getComponentBuilders().add(onCreateComponentBuilder);
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("NoSucMethodException while trying to build event method", e);
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

        subRepository.put(Context.class.getName(), contextVariableBuilderProvider.get());
        subRepository.put(android.app.Application.class.getName(), contextVariableBuilderProvider.get());
        subRepository.put(Resources.class.getName(), resourcesInjectionNodeBuilderProvider.get());

        return subRepository;

    }

    private void setupManifest(ProcessorContext context, String name, String label) {

        org.androidtransfuse.model.manifest.Application manifestApplication = applicationProvider.get();

        manifestApplication.setName(name);
        manifestApplication.setLabel(StringUtils.isBlank(label) ? null : label);

        context.getSourceManifest().getApplications().add(manifestApplication);
    }
}
