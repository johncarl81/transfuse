package org.androidtransfuse.analysis;

import android.content.Context;
import android.content.res.Configuration;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.Application;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.MethodCallbackGenerator;
import org.androidtransfuse.gen.componentBuilder.NoOpLayoutBuilder;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.processor.ManifestManager;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ApplicationAnalysis implements Analysis<ComponentDescriptor> {

    private InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private InjectionNodeBuilderRepository injectionNodeBuilderRepository;
    private Provider<org.androidtransfuse.model.manifest.Application> applicationProvider;
    private ComponentBuilderFactory componentBuilderFactory;
    private ASTClassFactory astClassFactory;
    private AnalysisContextFactory analysisContextFactory;
    private ManifestManager manifestManager;
    private InjectionBindingBuilder injectionBindingBuilder;

    @Inject
    public ApplicationAnalysis(InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory,
                               InjectionNodeBuilderRepository injectionNodeBuilderRepository,
                               Provider<org.androidtransfuse.model.manifest.Application> applicationProvider,
                               ComponentBuilderFactory componentBuilderFactory,
                               ASTClassFactory astClassFactory,
                               AnalysisContextFactory analysisContextFactory,
                               ManifestManager manifestManager, InjectionBindingBuilder injectionBindingBuilder) {
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.injectionNodeBuilderRepository = injectionNodeBuilderRepository;
        this.applicationProvider = applicationProvider;
        this.componentBuilderFactory = componentBuilderFactory;
        this.astClassFactory = astClassFactory;
        this.analysisContextFactory = analysisContextFactory;
        this.manifestManager = manifestManager;
        this.injectionBindingBuilder = injectionBindingBuilder;
    }

    public void emptyApplication() {
        setupManifest(android.app.Application.class.getName(), null);
    }

    public ComponentDescriptor analyze(ASTType astType) {
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

        //application generation profile
        setupApplicationProfile(applicationDescriptor, astType, analysisContext);

        //add manifest elements
        setupManifest(applicationDescriptor.getPackageClass().getFullyQualifiedName(), applicationAnnotation.label());

        return applicationDescriptor;
    }

    private void setupApplicationProfile(ComponentDescriptor applicationDescriptor, ASTType astType, AnalysisContext context) {

        try {
            ASTMethod onCreateASTMethod = astClassFactory.buildASTClassMethod(android.app.Application.class.getDeclaredMethod("onCreate"));
            //onCreate
            applicationDescriptor.setMethodBuilder(componentBuilderFactory.buildOnCreateMethodBuilder(onCreateASTMethod, new NoOpLayoutBuilder()));

            applicationDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildInjectionNodeFactory(astType, context));

            //onLowMemory
            applicationDescriptor.addGenerators(buildEventMethod("onLowMemory"));
            //onTerminate
            applicationDescriptor.addGenerators(buildEventMethod("onTerminate"));
            //onConfigurationChanged
            ASTMethod onConfigurationChangedASTMethod = astClassFactory.buildASTClassMethod(android.app.Application.class.getDeclaredMethod("onConfigurationChanged", Configuration.class));
            applicationDescriptor.addGenerators(
                    componentBuilderFactory.buildMethodCallbackGenerator("onConfigurationChanged",
                            componentBuilderFactory.buildSimpleMethodGenerator(onConfigurationChangedASTMethod, true)));

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
        injectionNodeBuilderRepository.putType(Context.class, injectionBindingBuilder.buildThis(Context.class));
        injectionNodeBuilderRepository.putType(android.app.Application.class, injectionBindingBuilder.buildThis((android.app.Application.class)));

        variableBuilderRepositoryFactory.addApplicationInjections(injectionNodeBuilderRepository);

        return injectionNodeBuilderRepository;

    }

    private void setupManifest(String name, String label) {

        org.androidtransfuse.model.manifest.Application manifestApplication = applicationProvider.get();

        manifestApplication.setName(name);
        manifestApplication.setLabel(StringUtils.isBlank(label) ? null : label);

        manifestManager.setApplication(manifestApplication);
    }
}
