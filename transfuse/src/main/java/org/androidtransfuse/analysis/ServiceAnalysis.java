/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.analysis;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import com.google.common.collect.ImmutableSet;
import com.sun.codemodel.*;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.GeneratorFactory;
import org.androidtransfuse.gen.componentBuilder.*;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.intentFactory.ServiceIntentFactoryStrategy;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.processor.ManifestManager;
import org.androidtransfuse.scope.ContextScopeHolder;
import org.androidtransfuse.util.TypeMirrorRunnable;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Map;

import static org.androidtransfuse.util.AnnotationUtil.checkBlank;
import static org.androidtransfuse.util.AnnotationUtil.checkDefault;
import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * Service related Analysis
 *
 * @author John Ericksen
 */
public class ServiceAnalysis implements Analysis<ComponentDescriptor> {

    private final Provider<InjectionNodeBuilderRepository> injectionNodeRepositoryProvider;
    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory;
    private final Provider<org.androidtransfuse.model.manifest.Service> manifestServiceProvider;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final AnalysisContextFactory analysisContextFactory;
    private final ASTClassFactory astClassFactory;
    private final ManifestManager manifestManager;
    private final IntentFilterFactory intentFilterBuilder;
    private final MetaDataBuilder metadataBuilder;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final ContextScopeComponentBuilder contextScopeComponentBuilder;
    private final GeneratorFactory generatorFactory;
    private final ListenerRegistrationGenerator listenerRegistrationGenerator;
    private final ObservesRegistrationGenerator observesExpressionDecorator;

    @Inject
    public ServiceAnalysis(Provider<InjectionNodeBuilderRepository> injectionNodeRepositoryProvider,
                           InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory,
                           Provider<org.androidtransfuse.model.manifest.Service> manifestServiceProvider,
                           ComponentBuilderFactory componentBuilderFactory,
                           AnalysisContextFactory analysisContextFactory,
                           ASTClassFactory astClassFactory, ManifestManager manifestManager,
                           IntentFilterFactory intentFilterBuilder,
                           MetaDataBuilder metadataBuilder,
                           InjectionBindingBuilder injectionBindingBuilder,
                           ASTTypeBuilderVisitor astTypeBuilderVisitor,
                           ContextScopeComponentBuilder contextScopeComponentBuilder,
                           GeneratorFactory generatorFactory,
                           ListenerRegistrationGenerator listenerRegistrationGenerator, ObservesRegistrationGenerator observesExpressionDecorator) {
        this.injectionNodeRepositoryProvider = injectionNodeRepositoryProvider;
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
        this.manifestServiceProvider = manifestServiceProvider;
        this.componentBuilderFactory = componentBuilderFactory;
        this.analysisContextFactory = analysisContextFactory;
        this.astClassFactory = astClassFactory;
        this.manifestManager = manifestManager;
        this.intentFilterBuilder = intentFilterBuilder;
        this.metadataBuilder = metadataBuilder;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.contextScopeComponentBuilder = contextScopeComponentBuilder;
        this.generatorFactory = generatorFactory;
        this.listenerRegistrationGenerator = listenerRegistrationGenerator;
        this.observesExpressionDecorator = observesExpressionDecorator;
    }

    public ComponentDescriptor analyze(ASTType input) {

        Service serviceAnnotation = input.getAnnotation(Service.class);
        PackageClass serviceClassName;
        ComponentDescriptor activityDescriptor = null;

        if (input.extendsFrom(astClassFactory.getType(android.app.Service.class))) {
            //vanilla Android Service
            PackageClass activityPackageClass = input.getPackageClass();
            serviceClassName = buildPackageClass(input, activityPackageClass.getClassName());
        } else {
            //generated Android Service
            serviceClassName = buildPackageClass(input, serviceAnnotation.name());

            TypeMirror type = getTypeMirror(new ServiceTypeMirrorRunnable(serviceAnnotation));

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

        PackageClass inputPackageClass = input.getPackageClass();

        if (StringUtils.isBlank(activityName)) {
            return inputPackageClass.append("Service");
        } else {
            return inputPackageClass.replaceName(activityName);
        }
    }

    private void setupManifest(String name, Service serviceAnnotation, ASTType type) {
        org.androidtransfuse.model.manifest.Service manifestService = buildService(name, serviceAnnotation);

        manifestService.setIntentFilters(intentFilterBuilder.buildIntentFilters(type));
        manifestService.setMetaData(metadataBuilder.buildMetaData(type));

        manifestManager.addService(manifestService);
    }

    protected org.androidtransfuse.model.manifest.Service buildService(String name, Service serviceAnnotation){
        org.androidtransfuse.model.manifest.Service manifestService = manifestServiceProvider.get();

        manifestService.setName(name);
        manifestService.setEnabled(checkDefault(serviceAnnotation.enabled(), true));
        manifestService.setExported(checkDefault(serviceAnnotation.exported(), true));
        manifestService.setIcon(checkBlank(serviceAnnotation.icon()));
        manifestService.setLabel(checkBlank(serviceAnnotation.label()));
        manifestService.setPermission(checkBlank(serviceAnnotation.permission()));
        manifestService.setProcess(checkBlank(serviceAnnotation.process()));

        return manifestService;
    }

    private void setupServiceProfile(ComponentDescriptor serviceDescriptor, ASTType astType, AnalysisContext context) {

        ASTMethod onCreateASTMethod = getASTMethod("onCreate");

        serviceDescriptor.setInitMethodBuilder(astClassFactory.getType(OnCreate.class), componentBuilderFactory.buildOnCreateMethodBuilder(onCreateASTMethod, new NoOpWindowFeatureBuilder(), new NoOpLayoutBuilder()));

        serviceDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildInjectionNodeFactory(ImmutableSet.<ASTAnnotation>of(), astType, context));

        serviceDescriptor.addGenerators(buildEventMethod(OnConfigurationChanged.class, "onConfigurationChanged", Configuration.class));
        //onDestroy
        serviceDescriptor.addGenerators(buildEventMethod(OnDestroy.class, "onDestroy"));
        //onLowMemory
        serviceDescriptor.addGenerators(buildEventMethod(OnLowMemory.class, "onLowMemory"));
        //onRebind(android.content.Intent intent)
        serviceDescriptor.addGenerators(buildEventMethod(OnRebind.class, "onRebind", Intent.class));
        //onTaskRemoved(Intent rootIntent)
        //serviceDescriptor.addGenerators(buildEventMethod(OnTaskRemoved.class, "onTaskRemoved", Intent.class));

        serviceDescriptor.addGenerators(new OnBindGenerator());

        serviceDescriptor.addGenerators(listenerRegistrationGenerator);

        serviceDescriptor.addGenerators(contextScopeComponentBuilder);

        serviceDescriptor.addRegistration(observesExpressionDecorator);

        serviceDescriptor.getGenerators().add(generatorFactory.buildStrategyGenerator(ServiceIntentFactoryStrategy.class));
    }

    private MethodCallbackGenerator buildEventMethod(Class<? extends Annotation> eventAnnotationClass, String methodName, Class... args) {
        ASTMethod method = getASTMethod(methodName, args);
        ASTType eventAnnotation = astClassFactory.getType(eventAnnotationClass);

        return componentBuilderFactory.buildMethodCallbackGenerator(eventAnnotation,
                componentBuilderFactory.buildMirroredMethodGenerator(method, true));
    }

    private ASTMethod getASTMethod(String methodName, Class... args) {
        return getASTMethod(android.app.Service.class, methodName, args);
    }

    private ASTMethod getASTMethod(Class type, String methodName, Class... args) {
        try {
            return astClassFactory.getMethod(type.getDeclaredMethod(methodName, args));
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while trying to reference method " + methodName, e);
        }
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(TypeMirror type) {

        InjectionNodeBuilderRepository injectionNodeBuilderRepository = injectionNodeRepositoryProvider.get();

        injectionNodeBuilderRepository.putType(Context.class, injectionBindingBuilder.buildThis(Context.class));
        injectionNodeBuilderRepository.putType(Application.class, injectionBindingBuilder.dependency(Context.class).invoke(Application.class, "getApplication").build());
        injectionNodeBuilderRepository.putType(android.app.Service.class, injectionBindingBuilder.buildThis(android.app.Service.class));
        injectionNodeBuilderRepository.putType(ContextScopeHolder.class, injectionBindingBuilder.buildThis(ContextScopeHolder.class));

        if (type != null && !type.toString().equals(android.app.Service.class.getName())) {
            ASTType serviceASTType = type.accept(astTypeBuilderVisitor, null);
            injectionNodeBuilderRepository.putType(serviceASTType, injectionBindingBuilder.buildThis(serviceASTType));
        }

        injectionNodeBuilderRepository.addRepository(injectionNodeBuilderRepositoryFactory.buildApplicationInjections());
        injectionNodeBuilderRepository.addRepository(injectionNodeBuilderRepositoryFactory.buildModuleConfiguration());

        return injectionNodeBuilderRepository;

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

    private static class OnBindGenerator implements ExpressionVariableDependentGenerator {
        @Override
        public void generate(JDefinedClass definedClass, MethodDescriptor methodDescriptor, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor, JExpression scopesExpression) {
            JMethod onBind = definedClass.method(JMod.PUBLIC, IBinder.class, "onBind");
            onBind.annotate(Override.class);
            onBind.param(Intent.class, "intent");

            onBind.body()._return(JExpr._null());
        }
    }
}