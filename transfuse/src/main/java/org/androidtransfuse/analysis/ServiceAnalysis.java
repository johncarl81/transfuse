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
import org.androidtransfuse.gen.componentBuilder.*;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.processor.ManifestManager;
import org.androidtransfuse.scope.ContextScopeHolder;
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
    private ContextScopeComponentBuilder contextScopeComponentBuilder;

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
                           ASTTypeBuilderVisitor astTypeBuilderVisitor, ContextScopeComponentBuilder contextScopeComponentBuilder) {
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
        this.contextScopeComponentBuilder = contextScopeComponentBuilder;
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


    private void setupServiceProfile(ComponentDescriptor serviceDescriptor, ASTType astType, AnalysisContext context) {

        ASTMethod onCreateASTMethod = getASTMethod("onCreate");

        serviceDescriptor.setMethodBuilder(componentBuilderFactory.buildOnCreateMethodBuilder(onCreateASTMethod, new NoOpLayoutBuilder()));

        serviceDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildInjectionNodeFactory(astType, context));

        //onStart onStart(android.content.Intent intent, int startId)
        serviceDescriptor.addGenerators(buildEventMethod("onStart", Intent.class, int.class));
        //onDestroy
        serviceDescriptor.addGenerators(buildEventMethod("onDestroy"));
        //onLowMemory
        serviceDescriptor.addGenerators(buildEventMethod("onLowMemory"));
        //onRebind onRebind(android.content.Intent intent)
        serviceDescriptor.addGenerators(buildEventMethod("onRebind", Intent.class));

        //todo: move this somewhere else
        serviceDescriptor.getComponentBuilders().add(new ComponentBuilder() {
            @Override
            public void build(JDefinedClass definedClass, ComponentDescriptor descriptor) {
                JMethod onBind = definedClass.method(JMod.PUBLIC, IBinder.class, "onBind");
                onBind.param(Intent.class, "intent");

                onBind.body()._return(JExpr._null());
            }
        });

        serviceDescriptor.getComponentBuilders().add(contextScopeComponentBuilder);
    }

    private MethodCallbackGenerator buildEventMethod(String name, Class... args) {
        return buildEventMethod(name, name, args);
    }

    private MethodCallbackGenerator buildEventMethod(String eventName, String methodName, Class... args) {
        ASTMethod method = getASTMethod(methodName, args);

        return componentBuilderFactory.buildMethodCallbackGenerator(eventName,
                componentBuilderFactory.buildMirroredMethodGenerator(method, true));
    }

    private ASTMethod getASTMethod(String methodName, Class... args){
        return getASTMethod(android.app.Service.class, methodName, args);
    }

    private ASTMethod getASTMethod(Class type, String methodName, Class... args){
        try{
            return astClassFactory.buildASTClassMethod(type.getDeclaredMethod(methodName, args));
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while trying to reference method " + methodName, e);
        }
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(TypeMirror type) {

        injectionNodeRepository.putType(Context.class, injectionBindingBuilder.buildThis(Context.class));
        injectionNodeRepository.putType(Application.class, injectionBindingBuilder.dependency(Context.class).invoke(Application.class, "getApplication").build());
        injectionNodeRepository.putType(android.app.Service.class, injectionBindingBuilder.buildThis(android.app.Service.class));
        injectionNodeRepository.putType(ContextScopeHolder.class, injectionBindingBuilder.buildThis(ContextScopeHolder.class));

        if (type != null) {
            ASTType serviceASTType = type.accept(astTypeBuilderVisitor, null);
            injectionNodeRepository.putType(serviceASTType, injectionBindingBuilder.buildThis(serviceASTType));
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