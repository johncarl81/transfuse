/**
 * Copyright 2012 John Ericksen
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

import android.content.Context;
import android.content.res.Configuration;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.ManifestBuilder;
import org.androidtransfuse.gen.componentBuilder.*;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.processor.ManifestManager;
import org.androidtransfuse.scope.ContextScopeHolder;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.annotation.Annotation;

import static org.androidtransfuse.util.AnnotationUtil.checkBlank;
import static org.androidtransfuse.util.AnnotationUtil.checkDefault;

/**
 * @author John Ericksen
 */
public class ApplicationAnalysis implements Analysis<ComponentDescriptor> {

    private final InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private final Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final ASTClassFactory astClassFactory;
    private final AnalysisContextFactory analysisContextFactory;
    private final ManifestManager manifestManager;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final ContextScopeComponentBuilder contextScopeComponentBuilder;
    private final ObservesRegistrationGenerator observesExpressionDecorator;
    private final ManifestBuilder manifestBuilder;

    @Inject
    public ApplicationAnalysis(InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory,
                               Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider,
                               ComponentBuilderFactory componentBuilderFactory,
                               ASTClassFactory astClassFactory,
                               AnalysisContextFactory analysisContextFactory,
                               ManifestManager manifestManager,
                               InjectionBindingBuilder injectionBindingBuilder,
                               ContextScopeComponentBuilder contextScopeComponentBuilder,
                               ObservesRegistrationGenerator observesExpressionDecorator,
                               ManifestBuilder manifestBuilder) {
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.injectionNodeBuilderRepositoryProvider = injectionNodeBuilderRepositoryProvider;
        this.componentBuilderFactory = componentBuilderFactory;
        this.astClassFactory = astClassFactory;
        this.analysisContextFactory = analysisContextFactory;
        this.manifestManager = manifestManager;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.contextScopeComponentBuilder = contextScopeComponentBuilder;
        this.observesExpressionDecorator = observesExpressionDecorator;
        this.manifestBuilder = manifestBuilder;
    }

    public ComponentDescriptor analyze(ASTType astType) {
        Application applicationAnnotation = astType.getAnnotation(Application.class);

        PackageClass inputType = astType.getPackageClass();
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
        applicationDescriptor.setInitMethodBuilder(OnCreate.class, componentBuilderFactory.buildOnCreateMethodBuilder(onCreateASTMethod, new NoOpLayoutBuilder()));

        applicationDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildInjectionNodeFactory(astType, context));

        //onLowMemory
        applicationDescriptor.addGenerators(buildEventMethod(OnLowMemory.class, "onLowMemory"));
        //onTerminate
        applicationDescriptor.addGenerators(buildEventMethod(OnTerminate.class, "onTerminate"));
        //onConfigurationChanged
        ASTMethod onConfigurationChangedASTMethod = getASTMethod("onConfigurationChanged", Configuration.class);
        applicationDescriptor.addGenerators(
                componentBuilderFactory.buildMethodCallbackGenerator(OnConfigurationChanged.class,
                        componentBuilderFactory.buildMirroredMethodGenerator(onConfigurationChangedASTMethod, true)));

        applicationDescriptor.addGenerators(contextScopeComponentBuilder);

        applicationDescriptor.addRegistration(observesExpressionDecorator);
    }

    private MethodCallbackGenerator buildEventMethod(Class<? extends Annotation> eventAnnotation, String methodName) {
        ASTMethod method = getASTMethod(methodName);

        return componentBuilderFactory.buildMethodCallbackGenerator(eventAnnotation,
                componentBuilderFactory.buildMirroredMethodGenerator(method, true));
    }

    private ASTMethod getASTMethod(String methodName, Class... args) {
        try {
            return astClassFactory.getMethod(android.app.Application.class.getDeclaredMethod(methodName, args));
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

    private void setupManifest(Application annotation, String name, String label) {

        org.androidtransfuse.model.manifest.Application manifestApplication = manifestBuilder.setupManifestApplication(name);

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
}
