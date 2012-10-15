package org.androidtransfuse.analysis;

import android.content.Context;
import android.content.res.Configuration;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.Application;
import org.androidtransfuse.annotations.UIOptions;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.ContextScopeComponentBuilder;
import org.androidtransfuse.gen.componentBuilder.MethodCallbackGenerator;
import org.androidtransfuse.gen.componentBuilder.NoOpLayoutBuilder;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.processor.ManifestManager;
import org.androidtransfuse.scope.ContextScopeHolder;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;

import static org.androidtransfuse.util.AnnotationUtil.checkBlank;
import static org.androidtransfuse.util.AnnotationUtil.checkDefault;

/**
 * @author John Ericksen
 */
public class ApplicationAnalysis implements Analysis<ComponentDescriptor> {

    private final InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private final Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider;
    private final Provider<org.androidtransfuse.model.manifest.Application> applicationProvider;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final ASTClassFactory astClassFactory;
    private final AnalysisContextFactory analysisContextFactory;
    private final ManifestManager manifestManager;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final ContextScopeComponentBuilder contextScopeComponentBuilder;

    @Inject
    public ApplicationAnalysis(InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory,
                               Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider,
                               Provider<org.androidtransfuse.model.manifest.Application> applicationProvider,
                               ComponentBuilderFactory componentBuilderFactory,
                               ASTClassFactory astClassFactory,
                               AnalysisContextFactory analysisContextFactory,
                               ManifestManager manifestManager,
                               InjectionBindingBuilder injectionBindingBuilder,
                               ContextScopeComponentBuilder contextScopeComponentBuilder) {
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.injectionNodeBuilderRepositoryProvider = injectionNodeBuilderRepositoryProvider;
        this.applicationProvider = applicationProvider;
        this.componentBuilderFactory = componentBuilderFactory;
        this.astClassFactory = astClassFactory;
        this.analysisContextFactory = analysisContextFactory;
        this.manifestManager = manifestManager;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.contextScopeComponentBuilder = contextScopeComponentBuilder;
    }

    public void emptyApplication() {
        setupManifest(android.app.Application.class.getName());
    }

    public ComponentDescriptor analyze(ASTType astType) {
        Application applicationAnnotation = astType.getAnnotation(Application.class);

        PackageClass inputType = new PackageClass(astType.getName());
        PackageClass applicationClassName;

        if (StringUtils.isBlank(applicationAnnotation.name())) {
            applicationClassName = inputType.append("Application");
        } else {
            applicationClassName = inputType.replaceName(applicationAnnotation.name());
        }

        ComponentDescriptor applicationDescriptor = new ComponentDescriptor(android.app.Application.class.getName(), applicationClassName);

        //analyze delegate
        AnalysisContext analysisContext = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap());

        //application generation profile
        setupApplicationProfile(applicationDescriptor, astType, analysisContext);

        //add manifest elements
        setupManifest(applicationAnnotation, applicationDescriptor.getPackageClass().getFullyQualifiedName(), applicationAnnotation.label());

        return applicationDescriptor;
    }

    private void setupApplicationProfile(ComponentDescriptor applicationDescriptor, ASTType astType, AnalysisContext context) {

        ASTMethod onCreateASTMethod = getASTMethod("onCreate");
        //onCreate
        applicationDescriptor.setMethodBuilder(componentBuilderFactory.buildOnCreateMethodBuilder(onCreateASTMethod, new NoOpLayoutBuilder()));

        applicationDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildInjectionNodeFactory(astType, context));

        //onLowMemory
        applicationDescriptor.addGenerators(buildEventMethod("onLowMemory"));
        //onTerminate
        applicationDescriptor.addGenerators(buildEventMethod("onTerminate"));
        //onConfigurationChanged
        ASTMethod onConfigurationChangedASTMethod = getASTMethod("onConfigurationChanged", Configuration.class);
        applicationDescriptor.addGenerators(
                componentBuilderFactory.buildMethodCallbackGenerator("onConfigurationChanged",
                        componentBuilderFactory.buildMirroredMethodGenerator(onConfigurationChangedASTMethod, true)));

        applicationDescriptor.addGenerators(contextScopeComponentBuilder);
    }

    private MethodCallbackGenerator buildEventMethod(String name) {
        return buildEventMethod(name, name);
    }

    private MethodCallbackGenerator buildEventMethod(String eventName, String methodName) {
        ASTMethod method = getASTMethod(methodName);

        return componentBuilderFactory.buildMethodCallbackGenerator(eventName,
                componentBuilderFactory.buildMirroredMethodGenerator(method, true));
    }

    private ASTMethod getASTMethod(String methodName, Class... args) {
        try {
            return astClassFactory.buildASTClassMethod(android.app.Application.class.getDeclaredMethod(methodName, args));
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while trying to reference method " + methodName, e);
        }
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap() {
        InjectionNodeBuilderRepository injectionNodeBuilderRepository = injectionNodeBuilderRepositoryProvider.get();

        injectionNodeBuilderRepository.putType(Context.class, injectionBindingBuilder.buildThis(Context.class));
        injectionNodeBuilderRepository.putType(android.app.Application.class, injectionBindingBuilder.buildThis((android.app.Application.class)));
        injectionNodeBuilderRepository.putType(ContextScopeHolder.class, injectionBindingBuilder.buildThis(ContextScopeHolder.class));

        variableBuilderRepositoryFactory.addApplicationInjections(injectionNodeBuilderRepository);

        variableBuilderRepositoryFactory.addModuleConfiguration(injectionNodeBuilderRepository);

        return injectionNodeBuilderRepository;

    }

    private void setupManifest(String name) {
        org.androidtransfuse.model.manifest.Application manifestApplication = buildManifest(name);

        manifestManager.setApplication(manifestApplication);
    }

    private void setupManifest(Application annotation, String name, String label) {

        org.androidtransfuse.model.manifest.Application manifestApplication = buildManifest(name);

        manifestApplication.setLabel(checkBlank(label));
        manifestApplication.setAllowTaskReparenting(checkDefault(annotation.allowTaskReparenting(), false));
        manifestApplication.setBackupAgent(checkBlank(annotation.backupAgent()));
        manifestApplication.setDebuggable(checkDefault(annotation.debuggable(), false));
        manifestApplication.setDescription(checkBlank(annotation.description()));
        manifestApplication.setEnabled(checkDefault(annotation.enabled(), true));
        manifestApplication.setHasCode(checkDefault(annotation.hasCode(), true));
        manifestApplication.setHardwareAccelerated(checkDefault(annotation.hardwareAccelerated(), false));
        manifestApplication.setIcon(checkBlank(annotation.icon()));
        manifestApplication.setKillAfterRestore(checkDefault(annotation.killAfterRestore(), true));
        manifestApplication.setLogo(checkBlank(annotation.logo()));
        manifestApplication.setManageSpaceActivity(checkBlank(annotation.manageSpaceActivity()));
        manifestApplication.setPermission(checkBlank(annotation.permission()));
        manifestApplication.setPersistent(checkDefault(annotation.persistent(), false));
        manifestApplication.setProcess(checkBlank(annotation.process()));
        manifestApplication.setRestoreAnyVersion(checkDefault(annotation.restoreAnyVersion(), false));
        manifestApplication.setTaskAffinity(checkBlank(annotation.taskAffinity()));
        manifestApplication.setTheme(checkBlank(annotation.theme()));
        manifestApplication.setUiOptions(checkDefault(annotation.uiOptions(), UIOptions.NONE));
        manifestManager.setApplication(manifestApplication);
    }

    private org.androidtransfuse.model.manifest.Application buildManifest(String name) {
        org.androidtransfuse.model.manifest.Application manifestApplication = applicationProvider.get();

        manifestApplication.setName(name);
        return manifestApplication;
    }
}
