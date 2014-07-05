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

import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.annotations.BroadcastReceiver;
import org.androidtransfuse.annotations.OnReceive;
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.experiment.ScopesGeneration;
import org.androidtransfuse.experiment.generators.BroadcastReceiverManifestEntryGenerator;
import org.androidtransfuse.experiment.generators.MethodCallbackGenerator;
import org.androidtransfuse.experiment.generators.OnCreateInjectionGenerator;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.util.AndroidLiterals;
import org.androidtransfuse.util.TypeMirrorRunnable;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;

import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * @author John Ericksen
 */
public class BroadcastReceiverAnalysis implements Analysis<ComponentDescriptor> {

    private final ASTClassFactory astClassFactory;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final AnalysisContextFactory analysisContextFactory;
    private final Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final BroadcastReceiverManifestEntryGenerator manifestEntryGenerator;
    private final ASTElementFactory astElementFactory;
    private final OnCreateInjectionGenerator.InjectionGeneratorFactory onCreateInjectionGeneratorFactory;
    private final ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory;

    @Inject
    public BroadcastReceiverAnalysis(ASTClassFactory astClassFactory,
                                     ASTTypeBuilderVisitor astTypeBuilderVisitor,
                                     AnalysisContextFactory analysisContextFactory,
                                     Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider,
                                     InjectionBindingBuilder injectionBindingBuilder,
                                     ComponentBuilderFactory componentBuilderFactory,
                                     BroadcastReceiverManifestEntryGenerator manifestEntryGenerator,
                                     ASTElementFactory astElementFactory,
                                     OnCreateInjectionGenerator.InjectionGeneratorFactory onCreateInjectionGeneratorFactory, ScopesGeneration.ScopesGenerationFactory scopesGenerationFactory) {
        this.astClassFactory = astClassFactory;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.analysisContextFactory = analysisContextFactory;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.manifestEntryGenerator = manifestEntryGenerator;
        this.injectionNodeBuilderRepositoryProvider = injectionNodeBuilderRepositoryProvider;
        this.componentBuilderFactory = componentBuilderFactory;
        this.astElementFactory = astElementFactory;
        this.onCreateInjectionGeneratorFactory = onCreateInjectionGeneratorFactory;
        this.scopesGenerationFactory = scopesGenerationFactory;
    }

    public ComponentDescriptor analyze(ASTType astType) {

        BroadcastReceiver broadcastReceiver = astType.getAnnotation(BroadcastReceiver.class);

        ComponentDescriptor receiverDescriptor;

        if (astType.extendsFrom(AndroidLiterals.BROADCAST_RECEIVER)) {
            //vanilla Android broadcast receiver
            PackageClass activityPackageClass = astType.getPackageClass();
            PackageClass receiverClassName = buildPackageClass(astType, activityPackageClass.getClassName());
            receiverDescriptor = new ComponentDescriptor(null, null, receiverClassName);
        } else {
            PackageClass receiverClassName = buildPackageClass(astType, broadcastReceiver.name());

            TypeMirror type = getTypeMirror(new ReceiverTypeRunnable(broadcastReceiver));
            ASTType receiverType = buildReceiverType(type);

            receiverDescriptor = new ComponentDescriptor(astType, receiverType, receiverClassName);

            InjectionNodeBuilderRepository injectionNodeBuilderRepository = injectionNodeBuilderRepositoryProvider.get();
            if(type != null) {
                ASTType applicationASTType = type.accept(astTypeBuilderVisitor, null);

                while(!applicationASTType.equals(AndroidLiterals.BROADCAST_RECEIVER) && applicationASTType.inheritsFrom(AndroidLiterals.BROADCAST_RECEIVER)){
                    injectionNodeBuilderRepository.putType(applicationASTType, injectionBindingBuilder.buildThis(applicationASTType));
                    applicationASTType = applicationASTType.getSuperClass();
                }
            }

            AnalysisContext analysisContext = analysisContextFactory.buildAnalysisContext(injectionNodeBuilderRepository);

            receiverDescriptor.getPreInjectionGenerators().add(scopesGenerationFactory.build(getASTMethod("onReceive", AndroidLiterals.CONTEXT, AndroidLiterals.INTENT)));
            receiverDescriptor.setInjectionGenerator(onCreateInjectionGeneratorFactory.build(getASTMethod("onReceive", AndroidLiterals.CONTEXT, AndroidLiterals.INTENT), astType));

            receiverDescriptor.getPostInjectionGenerators().add(buildEventMethod(OnReceive.class, "onReceive", AndroidLiterals.CONTEXT, AndroidLiterals.INTENT));

            //receiverDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildInjectionNodeFactory(ImmutableSet.<ASTAnnotation>of(), astType, analysisContext));
            receiverDescriptor.setAnalysisContext(analysisContext);
        }

        receiverDescriptor.getPreInjectionGenerators().add(manifestEntryGenerator);

        return receiverDescriptor;
    }

    private MethodCallbackGenerator buildEventMethod(Class<? extends Annotation> eventAnnotationClass, String methodName, ASTType... args) {

        ASTMethod method = getASTMethod(methodName, args);
        ASTType eventAnnotation = astClassFactory.getType(eventAnnotationClass);

        return componentBuilderFactory.buildMethodCallbackGenerator(eventAnnotation, method);
    }

    private ASTMethod getASTMethod(String methodName, ASTType... args) {
        return getASTMethod(AndroidLiterals.BROADCAST_RECEIVER, methodName, args);
    }

    private ASTMethod getASTMethod(ASTType type, String methodName, ASTType... args) {
        return astElementFactory.findMethod(type, methodName, args);
    }

    private ASTType buildReceiverType(TypeMirror type) {
        if (type != null && !type.toString().equals("java.lang.Object")) {
            return type.accept(astTypeBuilderVisitor, null);
        } else {
            return AndroidLiterals.BROADCAST_RECEIVER;
        }
    }

    private PackageClass buildPackageClass(ASTType astType, String className) {
        PackageClass inputPackageClass = astType.getPackageClass();

        if (StringUtils.isBlank(className)) {
            return inputPackageClass.append("BroadcastReceiver");
        } else {
            return inputPackageClass.replaceName(className);
        }
    }

    private static final class ReceiverTypeRunnable extends TypeMirrorRunnable<BroadcastReceiver> {
        private ReceiverTypeRunnable(BroadcastReceiver receiverAnnotation) {
            super(receiverAnnotation);
        }

        @Override
        public void run(BroadcastReceiver annotation) {
            //accessing this throws an exception, caught in TypeMirrorUtil
            annotation.type();
        }
    }
}
