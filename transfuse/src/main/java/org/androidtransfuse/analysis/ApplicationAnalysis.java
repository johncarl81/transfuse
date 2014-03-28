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

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.componentBuilder.*;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.processor.ManifestManager;
import org.androidtransfuse.scope.ContextScopeHolder;
import org.androidtransfuse.util.AndroidLiterals;
import org.androidtransfuse.util.TypeMirrorRunnable;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;

import static org.androidtransfuse.util.AnnotationUtil.checkBlank;
import static org.androidtransfuse.util.AnnotationUtil.checkDefault;
import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * @author John Ericksen
 */
public class ApplicationAnalysis implements Analysis<ComponentDescriptor> {

    private final Provider<org.androidtransfuse.model.manifest.Application> applicationProvider;
    private final InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private final Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final ASTClassFactory astClassFactory;
    private final ASTElementFactory astElementFactory;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final AnalysisContextFactory analysisContextFactory;
    private final ManifestManager manifestManager;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final ContextScopeComponentBuilder contextScopeComponentBuilder;
    private final ObservesRegistrationGenerator observesExpressionDecorator;
    private final MetaDataBuilder metadataBuilder;

    @Inject
    public ApplicationAnalysis(Provider<org.androidtransfuse.model.manifest.Application> applicationProvider, InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory,
                               Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider,
                               ComponentBuilderFactory componentBuilderFactory,
                               ASTClassFactory astClassFactory,
                               ASTElementFactory astElementFactory,
                               ASTTypeBuilderVisitor astTypeBuilderVisitor,
                               AnalysisContextFactory analysisContextFactory,
                               ManifestManager manifestManager,
                               InjectionBindingBuilder injectionBindingBuilder,
                               ContextScopeComponentBuilder contextScopeComponentBuilder,
                               ObservesRegistrationGenerator observesExpressionDecorator,
                               MetaDataBuilder metadataBuilder) {
        this.applicationProvider = applicationProvider;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.injectionNodeBuilderRepositoryProvider = injectionNodeBuilderRepositoryProvider;
        this.componentBuilderFactory = componentBuilderFactory;
        this.astClassFactory = astClassFactory;
        this.astElementFactory = astElementFactory;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.analysisContextFactory = analysisContextFactory;
        this.manifestManager = manifestManager;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.contextScopeComponentBuilder = contextScopeComponentBuilder;
        this.observesExpressionDecorator = observesExpressionDecorator;
        this.metadataBuilder = metadataBuilder;
    }

    public ComponentDescriptor analyze(ASTType astType) {
        Application applicationAnnotation = astType.getAnnotation(Application.class);


        PackageClass applicationClassName;
        ComponentDescriptor applicationDescriptor = null;

        if (astType.extendsFrom(AndroidLiterals.APPLICATION)) {
            //vanilla Android Application
            PackageClass activityPackageClass = astType.getPackageClass();
            applicationClassName = buildPackageClass(astType, activityPackageClass.getClassName());
        } else {

            applicationClassName = buildPackageClass(astType, applicationAnnotation.name());

            TypeMirror type = getTypeMirror(new ApplicationTypeMirrorRunnable(applicationAnnotation));

            String applicationType = type == null || type.toString().equals("java.lang.Object") ? AndroidLiterals.APPLICATION.getName() : type.toString();

            applicationDescriptor = new ComponentDescriptor(applicationType, applicationClassName);

            //analyze delegate
            AnalysisContext analysisContext = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(type));

            //application generation profile
            setupApplicationProfile(applicationDescriptor, astType, analysisContext);
        }

        //add manifest elements
        setupManifest(applicationAnnotation, astType, applicationClassName.getFullyQualifiedName(), applicationAnnotation.label());

        return applicationDescriptor;
    }

    private PackageClass buildPackageClass(ASTType input, String applicationNAme) {

        PackageClass inputPackageClass = input.getPackageClass();

        if (StringUtils.isBlank(applicationNAme)) {
            return inputPackageClass.append("Application");
        } else {
            return inputPackageClass.replaceName(applicationNAme);
        }
    }

    private void setupApplicationProfile(ComponentDescriptor applicationDescriptor, ASTType astType, AnalysisContext context) {

        ASTMethod onCreateASTMethod = getASTMethod("onCreate");
        //onCreate
        applicationDescriptor.setInitMethodBuilder(astClassFactory.getType(OnCreate.class), componentBuilderFactory.buildOnCreateMethodBuilder(onCreateASTMethod, new NoOpWindowFeatureBuilder(), new NoOpLayoutBuilder()));

        applicationDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildInjectionNodeFactory(ImmutableSet.<ASTAnnotation>of(), astType, context));

        //onLowMemory
        applicationDescriptor.addGenerators(buildEventMethod(OnLowMemory.class, "onLowMemory"));
        //onTerminate
        applicationDescriptor.addGenerators(buildEventMethod(OnTerminate.class, "onTerminate"));
        //onConfigurationChanged
        ASTMethod onConfigurationChangedASTMethod = getASTMethod("onConfigurationChanged", AndroidLiterals.CONTENT_CONFIGURATION);
        applicationDescriptor.addGenerators(
                componentBuilderFactory.buildMethodCallbackGenerator(astClassFactory.getType(OnConfigurationChanged.class),
                        componentBuilderFactory.buildMirroredMethodGenerator(onConfigurationChangedASTMethod, true)));

        applicationDescriptor.addGenerators(contextScopeComponentBuilder);

        applicationDescriptor.addRegistration(observesExpressionDecorator);
    }

    private MethodCallbackGenerator buildEventMethod(Class<? extends Annotation> eventAnnotationClass, String methodName) {
        ASTMethod method = getASTMethod(methodName);
        ASTType eventAnnotation = astClassFactory.getType(eventAnnotationClass);

        return componentBuilderFactory.buildMethodCallbackGenerator(eventAnnotation,
                componentBuilderFactory.buildMirroredMethodGenerator(method, true));
    }

    private ASTMethod getASTMethod(String methodName, ASTType... args) {
        return astElementFactory.findMethod(AndroidLiterals.APPLICATION, methodName, args);
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(TypeMirror applicationType) {
        InjectionNodeBuilderRepository injectionNodeBuilderRepository = injectionNodeBuilderRepositoryProvider.get();

        injectionNodeBuilderRepository.putType(AndroidLiterals.CONTEXT, injectionBindingBuilder.buildThis(AndroidLiterals.CONTEXT));
        injectionNodeBuilderRepository.putType(AndroidLiterals.APPLICATION, injectionBindingBuilder.buildThis((AndroidLiterals.APPLICATION)));
        injectionNodeBuilderRepository.putType(ContextScopeHolder.class, injectionBindingBuilder.buildThis(ContextScopeHolder.class));

        if(applicationType != null){
            ASTType applicationASTType = applicationType.accept(astTypeBuilderVisitor, null);

            while(!applicationASTType.equals(AndroidLiterals.APPLICATION) && applicationASTType.inheritsFrom(AndroidLiterals.APPLICATION)){
                injectionNodeBuilderRepository.putType(applicationASTType, injectionBindingBuilder.buildThis(applicationASTType));
                applicationASTType = applicationASTType.getSuperClass();
            }
        }


        injectionNodeBuilderRepository.addRepository(variableBuilderRepositoryFactory.buildApplicationInjections());
        injectionNodeBuilderRepository.addRepository(variableBuilderRepositoryFactory.buildModuleConfiguration());

        return injectionNodeBuilderRepository;

    }

    private void setupManifest(Application annotation, ASTType type, String name, String label) {

        org.androidtransfuse.model.manifest.Application manifestApplication = applicationProvider.get();

        manifestApplication.setName(name);

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
        manifestApplication.setAllowBackup(annotation.allowBackup());
        manifestApplication.setLargeHeap(checkDefault(annotation.largeHeap(), false));
        manifestApplication.setSupportsRtl(checkDefault(annotation.supportsRtl(), false));
        manifestApplication.setRestrictedAccountType(checkDefault(annotation.restrictedAccountType(), false));
        manifestApplication.setVmSafeMode(checkDefault(annotation.vmSafeMode(), false));
        manifestApplication.setTestOnly(checkDefault(annotation.testOnly(), false));
        manifestApplication.setRequiredAccountType(checkBlank(annotation.requiredAccountType()));

        manifestApplication.setMetaData(metadataBuilder.buildMetaData(type));

        manifestManager.addApplication(manifestApplication);
    }

    private static class ApplicationTypeMirrorRunnable extends TypeMirrorRunnable<Application> {
        public ApplicationTypeMirrorRunnable(Application ApplicationAnnotation) {
            super(ApplicationAnnotation);
        }

        @Override
        public void run(Application annotation) {
            annotation.type();
        }
    }
}
