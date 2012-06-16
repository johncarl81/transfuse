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
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.Service;
import org.androidtransfuse.gen.componentBuilder.*;
import org.androidtransfuse.gen.variableBuilder.ApplicationVariableInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
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

    private InjectionPointFactory injectionPointFactory;
    private InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private Provider<org.androidtransfuse.model.manifest.Service> manifestServiceProvider;
    private InjectionNodeBuilderRepository injectionNodeBuilders;
    private ComponentBuilderFactory componentBuilderFactory;
    private AnalysisContextFactory analysisContextFactory;
    private ASTClassFactory astClassFactory;
    private ManifestManager manifestManager;
    private IntentFilterBuilder intentFilterBuilder;
    private TypeMirrorUtil typeMirrorUtil;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private Provider<ApplicationVariableInjectionNodeBuilder> applicationVariableBuilderProvider;

    @Inject
    public ServiceAnalysis(InjectionPointFactory injectionPointFactory,
                           InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory,
                           Provider<org.androidtransfuse.model.manifest.Service> manifestServiceProvider,
                           InjectionNodeBuilderRepository injectionNodeBuilders,
                           ComponentBuilderFactory componentBuilderFactory,
                           AnalysisContextFactory analysisContextFactory,
                           ASTClassFactory astClassFactory, ManifestManager manifestManager,
                           IntentFilterBuilder intentFilterBuilder,
                           TypeMirrorUtil typeMirrorUtil,
                           VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                           Provider<ApplicationVariableInjectionNodeBuilder> applicationVariableBuilderProvider) {
        this.injectionPointFactory = injectionPointFactory;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.manifestServiceProvider = manifestServiceProvider;
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.componentBuilderFactory = componentBuilderFactory;
        this.analysisContextFactory = analysisContextFactory;
        this.astClassFactory = astClassFactory;
        this.manifestManager = manifestManager;
        this.intentFilterBuilder = intentFilterBuilder;
        this.typeMirrorUtil = typeMirrorUtil;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.applicationVariableBuilderProvider = applicationVariableBuilderProvider;
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

            AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap());

            activityDescriptor = new ComponentDescriptor(serviceType, serviceClassName);
            InjectionNode injectionNode = injectionPointFactory.buildInjectionNode(input, context);

            //application generation profile
            setupServiceProfile(activityDescriptor, injectionNode);
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


    private void setupServiceProfile(ComponentDescriptor activityDescriptor, InjectionNode injectionNode) {
        try {
            ASTMethod onCreateASTMethod = astClassFactory.buildASTClassMethod(android.app.Service.class.getDeclaredMethod("onCreate"));

            OnCreateComponentBuilder onCreateComponentBuilder = componentBuilderFactory.buildOnCreateComponentBuilder(injectionNode, new NoOpLayoutBuilder(), onCreateASTMethod);


            //onLowMemory
            //onCreateComponentBuilder.addMethodCallbackBuilder(buildEventMethod("onStart"));
            //onTerminate
            onCreateComponentBuilder.addMethodCallbackBuilder(buildEventMethod("onDestroy"));
            //onTerminate
            onCreateComponentBuilder.addMethodCallbackBuilder(buildEventMethod("onLowMemory"));
            //onTerminate
            //onCreateComponentBuilder.addMethodCallbackBuilder(buildEventMethod("onRebind"));

            activityDescriptor.getComponentBuilders().add(onCreateComponentBuilder);

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

    private InjectionNodeBuilderRepository buildVariableBuilderMap() {

        InjectionNodeBuilderRepository subRepository = variableBuilderRepositoryFactory.buildRepository(injectionNodeBuilders);


        subRepository.put(Context.class.getName(), variableInjectionBuilderFactory.buildContextVariableInjectionNodeBuilder(Context.class));
        subRepository.put(Application.class.getName(), applicationVariableBuilderProvider.get());
        subRepository.put(android.app.Activity.class.getName(), variableInjectionBuilderFactory.buildContextVariableInjectionNodeBuilder(android.app.Activity.class));
        /*subRepository.put(Resources.class.getName(), resourcesInjectionNodeBuilderProvider.get());

        //todo: map inheritance of activity type?
        if (activityType != null) {
            subRepository.put(activityType.toString(), variableInjectionBuilderFactory.buildContextVariableInjectionNodeBuilder(android.app.Activity.class));
        }*/

        return subRepository;

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