/**
 * Copyright 2011-2015 John Ericksen
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

import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.BroadcastReceiver;
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.experiment.ScopesGeneration;
import org.androidtransfuse.experiment.generators.BroadcastReceiverManifestEntryGenerator;
import org.androidtransfuse.experiment.generators.OnCreateInjectionGenerator;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.gen.variableBuilder.ProviderInjectionNodeBuilderFactory;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.scope.ApplicationScope;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;
import javax.lang.model.type.TypeMirror;

import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * @author John Ericksen
 */
public class BroadcastReceiverAnalysis implements Analysis<ComponentDescriptor> {

    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final AnalysisContextFactory analysisContextFactory;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final BroadcastReceiverManifestEntryGenerator manifestEntryGenerator;
    private final ASTElementFactory astElementFactory;
    private final ProviderInjectionNodeBuilderFactory providerInjectionNodeBuilder;
    private final OnCreateInjectionGenerator.InjectionGeneratorFactory onCreateInjectionGeneratorFactory;
    private final ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory;
    private final ComponentAnalysis componentAnalysis;

    @Inject
    public BroadcastReceiverAnalysis(InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory,
                                     ASTTypeBuilderVisitor astTypeBuilderVisitor,
                                     AnalysisContextFactory analysisContextFactory,
                                     InjectionBindingBuilder injectionBindingBuilder,
                                     BroadcastReceiverManifestEntryGenerator manifestEntryGenerator,
                                     ASTElementFactory astElementFactory,
                                     ProviderInjectionNodeBuilderFactory providerInjectionNodeBuilder,
                                     OnCreateInjectionGenerator.InjectionGeneratorFactory onCreateInjectionGeneratorFactory,
                                     ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory,
                                     ComponentAnalysis componentAnalysis) {
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.analysisContextFactory = analysisContextFactory;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.manifestEntryGenerator = manifestEntryGenerator;
        this.astElementFactory = astElementFactory;
        this.providerInjectionNodeBuilder = providerInjectionNodeBuilder;
        this.onCreateInjectionGeneratorFactory = onCreateInjectionGeneratorFactory;
        this.scopesGenerationFactory = scopesGenerationFactory;
        this.componentAnalysis = componentAnalysis;
    }

    public ComponentDescriptor analyze(ASTType astType) {

        BroadcastReceiver broadcastReceiverAnnotation = astType.getAnnotation(BroadcastReceiver.class);

        ComponentDescriptor receiverDescriptor;

        if (astType.extendsFrom(AndroidLiterals.BROADCAST_RECEIVER)) {
            //vanilla Android broadcast receiver
            PackageClass activityPackageClass = astType.getPackageClass();
            PackageClass receiverClassName = componentAnalysis.buildComponentPackageClass(astType, activityPackageClass.getClassName(), "BroadcastReceiver");
            receiverDescriptor = new ComponentDescriptor(astType, null, receiverClassName);
        } else {
            PackageClass receiverClassName = componentAnalysis.buildComponentPackageClass(astType, broadcastReceiverAnnotation.name(), "BroadcastReceiver");

            TypeMirror type = getTypeMirror(broadcastReceiverAnnotation, "type");
            ASTType receiverType = type == null || type.toString().equals("java.lang.Object") ? AndroidLiterals.BROADCAST_RECEIVER : type.accept(astTypeBuilderVisitor, null);

            InjectionNodeBuilderRepository injectionNodeBuilderRepository = componentAnalysis.setupInjectionNodeBuilderRepository(receiverType, BroadcastReceiver.class);
            ASTType applicationScopeType = astElementFactory.getType(ApplicationScope.ApplicationScopeQualifier.class);
            ASTType applicationProvider = astElementFactory.getType(ApplicationScope.ApplicationProvider.class);
            injectionNodeBuilderRepository.putType(AndroidLiterals.APPLICATION, providerInjectionNodeBuilder.builderProviderBuilder(applicationProvider));
            injectionNodeBuilderRepository.putScoped(new InjectionSignature(AndroidLiterals.APPLICATION), applicationScopeType);

            injectionNodeBuilderRepository.addRepository(
                    injectionNodeBuilderRepositoryFactory.buildApplicationInjections());

            injectionNodeBuilderRepository.addRepository(
                    injectionNodeBuilderRepositoryFactory.buildModuleConfiguration());

            if(type != null) {
                ASTType applicationASTType = type.accept(astTypeBuilderVisitor, null);

                while(!applicationASTType.equals(AndroidLiterals.BROADCAST_RECEIVER) && applicationASTType.inheritsFrom(AndroidLiterals.BROADCAST_RECEIVER)){
                    injectionNodeBuilderRepository.putType(applicationASTType, injectionBindingBuilder.buildThis(applicationASTType));
                    applicationASTType = applicationASTType.getSuperClass();
                }
            }

            AnalysisContext analysisContext = analysisContextFactory.buildAnalysisContext(injectionNodeBuilderRepository);

            receiverDescriptor = new ComponentDescriptor(astType, receiverType, receiverClassName, analysisContext);

            receiverDescriptor.getGenerators().add(scopesGenerationFactory.build(getASTMethod("onReceive", AndroidLiterals.CONTEXT, AndroidLiterals.INTENT)));

            receiverDescriptor.getGenerators().add(onCreateInjectionGeneratorFactory.build(getASTMethod("onReceive", AndroidLiterals.CONTEXT, AndroidLiterals.INTENT), astType));

            componentAnalysis.setupGenerators(receiverDescriptor, receiverType, BroadcastReceiver.class);
        }

        receiverDescriptor.getGenerators().add(manifestEntryGenerator);

        return receiverDescriptor;
    }

    private ASTMethod getASTMethod(String methodName, ASTType... args) {
        return getASTMethod(AndroidLiterals.BROADCAST_RECEIVER, methodName, args);
    }

    private ASTMethod getASTMethod(ASTType type, String methodName, ASTType... args) {
        return astElementFactory.findMethod(type, methodName, args);
    }
}
