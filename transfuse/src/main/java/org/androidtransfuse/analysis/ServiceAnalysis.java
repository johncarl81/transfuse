package org.androidtransfuse.analysis;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.adapter.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.Service;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilder;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.MethodCallbackGenerator;
import org.androidtransfuse.gen.componentBuilder.NoOpLayoutBuilder;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.processor.ManifestManager;
import org.androidtransfuse.util.TypeMirrorRunnable;
import org.androidtransfuse.util.TypeMirrorUtil;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;

/**
 * Service related Analysis
 *
 * @author John Ericksen
 */
public class ServiceAnalysis implements Analysis<ComponentDescriptor> {

    private InjectionNodeBuilderRepository injectionNodeRepository;
    private InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory;
    private Provider<org.androidtransfuse.model.manifest.Service> manifestServiceProvider;
    private ComponentBuilderFactory componentBuilderFactory;
    private AnalysisContextFactory analysisContextFactory;
    private ASTClassFactory astClassFactory;
    private ManifestManager manifestManager;
    private IntentFilterBuilder intentFilterBuilder;
    private TypeMirrorUtil typeMirrorUtil;
    private MetaDataBuilder metadataBuilder;
    private InjectionBindingBuilder injectionBindingBuilder;
    private ASTTypeBuilderVisitor astTypeBuilderVisitor;

    @Inject
    public ServiceAnalysis(InjectionNodeBuilderRepository injectionNodeRepository,
                           InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory,
                           Provider<org.androidtransfuse.model.manifest.Service> manifestServiceProvider,
                           ComponentBuilderFactory componentBuilderFactory,
                           AnalysisContextFactory analysisContextFactory,
                           ASTClassFactory astClassFactory, ManifestManager manifestManager,
                           IntentFilterBuilder intentFilterBuilder,
                           TypeMirrorUtil typeMirrorUtil,
                           MetaDataBuilder metadataBuilder,
                           InjectionBindingBuilder injectionBindingBuilder,
                           ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        this.injectionNodeRepository = injectionNodeRepository;
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
        this.manifestServiceProvider = manifestServiceProvider;
        this.componentBuilderFactory = componentBuilderFactory;
        this.analysisContextFactory = analysisContextFactory;
        this.astClassFactory = astClassFactory;
        this.manifestManager = manifestManager;
        this.intentFilterBuilder = intentFilterBuilder;
        this.typeMirrorUtil = typeMirrorUtil;
        this.metadataBuilder = metadataBuilder;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
    }

    public ComponentDescriptor analyze(ASTType input) {

        Service serviceAnnotation = input.getAnnotation(Service.class);
        PackageClass serviceClassName;
        ComponentDescriptor activityDescriptor = null;

        if (input.extendsFrom(astClassFactory.buildASTClassType(android.app.Service.class))) {
            //vanilla Android Service
            PackageClass activityPackageClass = new PackageClass(input.getName());
            serviceClassName = buildPackageClass(input, activityPackageClass.getClassName());
        } else {
            //generated Android Service
            serviceClassName = buildPackageClass(input, serviceAnnotation.name());

            TypeMirror type = typeMirrorUtil.getTypeMirror(new ServiceTypeMirrorRunnable(serviceAnnotation));

            String serviceType = type == null ? android.app.Service.class.getName() : type.toString();

            AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(type));

            activityDescriptor = new ComponentDescriptor(serviceType, serviceClassName);

            //application generation profile
            setupServiceProfile(activityDescriptor, input, context);
        }

        //add manifest elements
        setupManifest(serviceClassName.getFullyQualifiedName(), serviceAnnotation, input);

        return activityDescriptor;
    }

    private PackageClass buildPackageClass(ASTType input, String activityName) {

        PackageClass inputPackageClass = new PackageClass(input.getName());

        if (StringUtils.isBlank(activityName)) {
            return inputPackageClass.add("Service");
        } else {
            return inputPackageClass.replaceName(activityName);
        }
    }

    private void setupManifest(String name, Service serviceAnnotation, ASTType type) {
        org.androidtransfuse.model.manifest.Service manifestService = manifestServiceProvider.get();

        manifestService.setName(name);
        manifestService.setEnabled(checkDefault(serviceAnnotation.enabled(), true));
        manifestService.setExported(checkDefault(serviceAnnotation.exported(), true));
        manifestService.setIcon(checkBlank(serviceAnnotation.icon()));
        manifestService.setLabel(checkBlank(serviceAnnotation.label()));
        manifestService.setIntentFilters(intentFilterBuilder.buildIntentFilters(type));
        manifestService.setMetaData(metadataBuilder.buildMetaData(type));
        manifestService.setPermission(checkBlank(serviceAnnotation.permission()));
        manifestService.setProcess(checkBlank(serviceAnnotation.process()));

        manifestManager.addService(manifestService);
    }

    private <T> T checkDefault(T input, T defaultValue) {
        if (input.equals(defaultValue)) {
            return null;
        }
        return input;
    }

    private String checkBlank(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        return input;
    }


    private void setupServiceProfile(ComponentDescriptor activityDescriptor, ASTType astType, AnalysisContext context) {
        try {
            ASTMethod onCreateASTMethod = astClassFactory.buildASTClassMethod(android.app.Service.class.getDeclaredMethod("onCreate"));

            activityDescriptor.setMethodBuilder(componentBuilderFactory.buildOnCreateMethodBuilder(onCreateASTMethod, new NoOpLayoutBuilder()));

            activityDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildInjectionNodeFactory(astType, context));

            //onStart onStart(android.content.Intent intent, int startId)
            ASTMethod onStartASTMethod = astClassFactory.buildASTClassMethod(android.app.Service.class.getDeclaredMethod("onStart", Intent.class, int.class));
            activityDescriptor.addGenerators(
                    componentBuilderFactory.buildMethodCallbackGenerator("onStart",
                            componentBuilderFactory.buildSimpleMethodGenerator(onStartASTMethod, true)));
            //onDestroy
            activityDescriptor.addGenerators(buildEventMethod("onDestroy"));
            //onLowMemory
            activityDescriptor.addGenerators(buildEventMethod("onLowMemory"));
            //onRebind onRebind(android.content.Intent intent)
            ASTMethod onRebindASTMethod = astClassFactory.buildASTClassMethod(android.app.Service.class.getDeclaredMethod("onRebind", Intent.class));
            activityDescriptor.addGenerators(
                    componentBuilderFactory.buildMethodCallbackGenerator("onRebind",
                            componentBuilderFactory.buildSimpleMethodGenerator(onRebindASTMethod, true)));

            //todo: move this somewhere else
            activityDescriptor.getComponentBuilders().add(new ComponentBuilder() {
                @Override
                public void build(JDefinedClass definedClass, ComponentDescriptor descriptor) {
                    JMethod onBind = definedClass.method(JMod.PUBLIC, IBinder.class, "onBind");
                    onBind.param(Intent.class, "intent");

                    onBind.body()._return(JExpr._null());
                }
            });


        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("Unable to find Service onCreate method", e);
        }
    }

    private MethodCallbackGenerator buildEventMethod(String name) throws NoSuchMethodException {
        return buildEventMethod(name, name);
    }

    private MethodCallbackGenerator buildEventMethod(String eventName, String methodName) throws NoSuchMethodException {
        ASTMethod method = astClassFactory.buildASTClassMethod(android.app.Service.class.getMethod(methodName));

        return componentBuilderFactory.buildMethodCallbackGenerator(eventName,
                componentBuilderFactory.buildSimpleMethodGenerator(method, true));
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(TypeMirror type) {

        injectionNodeRepository.putType(Context.class, injectionBindingBuilder.buildThis(Context.class));
        injectionNodeRepository.putType(Application.class, injectionBindingBuilder.dependency(Context.class).invoke(Application.class, "getApplication").build());
        injectionNodeRepository.putType(android.app.Service.class, injectionBindingBuilder.buildThis(android.app.Service.class));

        if (type != null) {
            ASTType activityASTType = type.accept(astTypeBuilderVisitor, null);
            injectionNodeRepository.putType(activityASTType, injectionBindingBuilder.buildThis(android.app.Service.class));
        }

        injectionNodeBuilderRepositoryFactory.addApplicationInjections(injectionNodeRepository);

        return injectionNodeRepository;

    }

    private static class ServiceTypeMirrorRunnable extends TypeMirrorRunnable<Service> {
        public ServiceTypeMirrorRunnable(Service serviceAnnotation) {
            super(serviceAnnotation);
        }

        @Override
        public void run(Service annotation) {
            annotation.type();
        }
    }
}