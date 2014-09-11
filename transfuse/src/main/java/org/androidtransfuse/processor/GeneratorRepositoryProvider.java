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
package org.androidtransfuse.processor;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.*;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.config.ScopesGeneratorWorker;
import org.androidtransfuse.experiment.ComponentDescriptor;
import org.androidtransfuse.gen.AnalysisGenerationFactory;
import org.androidtransfuse.gen.ComponentsGenerator;
import org.androidtransfuse.gen.PackageHelperGeneratorAdapter;
import org.androidtransfuse.transaction.*;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Configures the Processor chain
 *
 *[ditaa]
 * --
 *     +---------+             +--------------+    +----------+          +-----------------+
 * -+->| Modules +-------+--+->| Component(s) +-+->| Manifest |---+-+-+->| Package Helpers |
 *  |  +---------+       |  |  +--------------+ |  +----------+   | | |  +-----------------+
 *  |                    |  |                   |                 | | |
 *  |  +---------------+ |  |                   |  +------------+ | | |  +-----------------+
 *  +->| ImplementedBy +-+  |                   +->| Components |-+ | +->| Virtual Proxies |
 *     +---------------+    |                      +------------+   | |  +-----------------+
 *                          |                                       | |
 *                          |  +-------------+     +-----------+    | |  +-------------+
 *                          +->| Factory(s)  +---->| Factories +----+ +->| Scopes Util |
 *                             +-------------+     +-----------+         +-------------+
 * --
 * @author John Ericksen
 */
public class GeneratorRepositoryProvider implements Provider<GeneratorRepository> {

    private final FactoryProcessor factoryProcessor;
    private final AnalysisGenerationFactory analysisGenerationFactory;
    private final Provider<ActivityAnalysis> activityAnalysisProvider;
    private final Provider<BroadcastReceiverAnalysis> broadcastReceiverAnalysisProvider;
    private final Provider<ServiceAnalysis> serviceAnalysisProvider;
    private final Provider<FragmentAnalysis> fragmentAnalysisProvider;
    private final Provider<ApplicationAnalysis> applicationAnalysisProvider;
    private final AnalysisGenerationTransactionProcessorBuilderFactory processorFactory;
    private final GenerateModuleProcessor generateModuleProcessor;
    private final Provider<PackageHelperGeneratorAdapter> packageHelperTransactionWorkerProvider;
    private final ModuleProcessorBuilder moduleProcessorBuilder;
    private final ImplementedByProcessorBuilder implementedByProcessorBuilder;
    private final TransactionProcessorPool<Map<Provider<ASTType>, JDefinedClass>, JDefinedClass> componentsRepositoryProcessor;
    private final Provider<ComponentsGenerator> componentsGeneratorProvider;
    private final Provider<VirtualProxyTransactionWorker> virtualProxyTransactionWorkerProvider;
    private final Provider<ScopesGeneratorWorker> scopesGeneratorWorkerProvider;
    private final ScopedTransactionBuilder scopedTransactionBuilder;

    @Inject
    public GeneratorRepositoryProvider(FactoryProcessor factoryProcessor,
                                       AnalysisGenerationFactory analysisGenerationFactory,
                                       Provider<ActivityAnalysis> activityAnalysisProvider,
                                       Provider<BroadcastReceiverAnalysis> broadcastReceiverAnalysisProvider,
                                       Provider<ServiceAnalysis> serviceAnalysisProvider,
                                       Provider<FragmentAnalysis> fragmentAnalysisProvider,
                                       Provider<ApplicationAnalysis> applicationAnalysisProvider,
                                       AnalysisGenerationTransactionProcessorBuilderFactory processorFactory,
                                       GenerateModuleProcessor generateModuleProcessor,
                                       Provider<PackageHelperGeneratorAdapter> packageHelperTransactionWorkerProvider,
                                       ModuleProcessorBuilder moduleProcessorBuilder,
                                       ImplementedByProcessorBuilder implementedByProcessorBuilder,
                                       TransactionProcessorPool<Map<Provider<ASTType>, JDefinedClass>, JDefinedClass> componentsRepositoryProcessor,
                                       Provider<ComponentsGenerator> componentsGeneratorProvider,
                                       Provider<VirtualProxyTransactionWorker> virtualProxyTransactionWorkerProvider,
                                       Provider<ScopesGeneratorWorker> scopesGeneratorWorkerProvider,
                                       ScopedTransactionBuilder scopedTransactionBuilder) {
        this.factoryProcessor = factoryProcessor;
        this.analysisGenerationFactory = analysisGenerationFactory;
        this.activityAnalysisProvider = activityAnalysisProvider;
        this.broadcastReceiverAnalysisProvider = broadcastReceiverAnalysisProvider;
        this.serviceAnalysisProvider = serviceAnalysisProvider;
        this.fragmentAnalysisProvider = fragmentAnalysisProvider;
        this.applicationAnalysisProvider = applicationAnalysisProvider;
        this.processorFactory = processorFactory;
        this.generateModuleProcessor = generateModuleProcessor;
        this.packageHelperTransactionWorkerProvider = packageHelperTransactionWorkerProvider;
        this.moduleProcessorBuilder = moduleProcessorBuilder;
        this.implementedByProcessorBuilder = implementedByProcessorBuilder;
        this.componentsRepositoryProcessor = componentsRepositoryProcessor;
        this.componentsGeneratorProvider = componentsGeneratorProvider;
        this.virtualProxyTransactionWorkerProvider = virtualProxyTransactionWorkerProvider;
        this.scopesGeneratorWorkerProvider = scopesGeneratorWorkerProvider;
        this.scopedTransactionBuilder = scopedTransactionBuilder;
    }

    @Override
    public GeneratorRepository get() {

        ImmutableMap.Builder<Class<? extends Annotation>, TransactionProcessorBuilder<Provider<ASTType>, ?>> processorMapBuilder = ImmutableMap.builder();
        ImmutableSet.Builder<TransactionProcessor<Provider<ASTType>, JDefinedClass>> componentProcessors = ImmutableSet.builder();

        // Module and ImplementedBy configuration processing
        processorMapBuilder.put(TransfuseModule.class, moduleProcessorBuilder);
        processorMapBuilder.put(ImplementedBy.class, implementedByProcessorBuilder);

        TransactionProcessor<Provider<ASTType>, Void> configurationProcessors = new TransactionProcessorComposite<Provider<ASTType>, Void>(
                ImmutableSet.of(moduleProcessorBuilder.getTransactionProcessor(),
                        implementedByProcessorBuilder.getTransactionProcessor()));

        // Component processing
        Map<Class<? extends Annotation>, Provider<? extends Analysis<ComponentDescriptor>>> analyzers =
                new HashMap<Class<? extends Annotation>, Provider<? extends Analysis<ComponentDescriptor>>>();
        analyzers.put(Application.class, applicationAnalysisProvider);
        analyzers.put(Activity.class, activityAnalysisProvider);
        analyzers.put(BroadcastReceiver.class, broadcastReceiverAnalysisProvider);
        analyzers.put(Service.class, serviceAnalysisProvider);
        analyzers.put(Fragment.class, fragmentAnalysisProvider);

        for (Map.Entry<Class<? extends Annotation>, Provider<? extends Analysis<ComponentDescriptor>>> providerEntry : analyzers.entrySet()) {
            Provider<TransactionWorker<Provider<ASTType>, JDefinedClass>> workerProvider = analysisGenerationFactory.buildAnalysisGenerationProvider(providerEntry.getValue());
            AnalysisGenerationTransactionProcessorBuilder processorBuilder = processorFactory.buildBuilder(workerProvider);

            componentProcessors.add(processorBuilder.getTransactionProcessor());

            processorMapBuilder.put(providerEntry.getKey(), processorBuilder);
        }

        TransactionProcessor<Provider<ASTType>, JDefinedClass> componentsProcessor = new TransactionProcessorComposite<Provider<ASTType>, JDefinedClass>(componentProcessors.build());

        // Manifest processing (depends on components)
        TransactionProcessor<Void, Void> manifestProcessor = new TransactionProcessorPredefined(ImmutableSet.of(new Transaction<Void, Void>(generateModuleProcessor)));
        TransactionProcessor<Void, Void> componentProcessorCompletion = new TransactionProcessorChain(

                new TransactionProcessorChannel<Provider<ASTType>, JDefinedClass, JDefinedClass>(
                        componentsProcessor,
                        componentsRepositoryProcessor,
                        scopedTransactionBuilder.buildFactory(componentsGeneratorProvider)),
                manifestProcessor);

        ImmutableSet.Builder<TransactionProcessor<?, ?>> configurationDependentBuilders = ImmutableSet.builder();

        configurationDependentBuilders.add(componentProcessorCompletion);

        processorMapBuilder.put(Factory.class, factoryProcessor);

        // Package Helper processing (to be run last)
        TransactionProcessor<Void, Void> packageHelperProcessor = new TransactionProcessorPredefined(
                ImmutableSet.of(
                        scopedTransactionBuilder.build(packageHelperTransactionWorkerProvider),
                        scopedTransactionBuilder.build(virtualProxyTransactionWorkerProvider),
                        scopedTransactionBuilder.build(scopesGeneratorWorkerProvider)));

        TransactionProcessor<?, ?> configurationDependentProcessors =
                new TransactionProcessorComposite(configurationDependentBuilders.build());

        TransactionProcessor<Void, Void> processor = new TransactionProcessorChain(configurationProcessors,
                new TransactionProcessorChain(factoryProcessor.getTransactionProcessor(),
                new TransactionProcessorChain(configurationDependentProcessors, packageHelperProcessor)));

        return new GeneratorRepository(processorMapBuilder.build(), processor);
    }
}
