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
import com.google.inject.ImplementedBy;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.*;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.AnalysisGenerationFactory;
import org.androidtransfuse.model.ComponentDescriptor;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Configures the Processor chain
 *
 *     +---------+             +--------------+    +----------+        +-----------------+
 * -+->| Modules +-------+--+->| Component(s) +-+->| Manifest |---+-+->| Package Helpers |
 *  |  +---------+       |  |  +--------------+ |  +----------+   | |  +-----------------+
 *  |  +---------------+ |  |                   |  +------------+ | |
 *  +->| ImplementedBy +-+  |                   +->| Components |-+ |
 *     +---------------+    |                      +------------+   |
 *                          |  +-------------+  +-----------+       |
 *                          +->| Injector(s) +->| Injectors +-------+
 *                             +-------------+  +-----------+
 *
 * @author John Ericksen
 */
public class GeneratorRepositoryProvider implements Provider<GeneratorRepository> {

    private final InjectorProcessor injectorProcessor;
    private final AnalysisGenerationFactory analysisGenerationFactory;
    private final Provider<ActivityAnalysis> activityAnalysisProvider;
    private final Provider<BroadcastReceiverAnalysis> broadcastReceiverAnalysisProvider;
    private final Provider<ServiceAnalysis> serviceAnalysisProvider;
    private final Provider<FragmentAnalysis> fragmentAnalysisProvider;
    private final Provider<ApplicationAnalysis> applicationAnalysisProvider;
    private final AnalysisGenerationTransactionProcessorBuilderFactory processorFactory;
    private final GenerateModuleProcessor generateModuleProcessor;
    private final PackageHelperTransactionFactory packageHelperTransactionFactory;
    private final ModuleProcessorBuilder moduleProcessorBuilder;
    private final ImplementedByProcessorBuilder implementedByProcessorBuilder;
    private final TransactionProcessorPool<Map<Provider<ASTType>, JDefinedClass>, Void> componentsRepositoryProcessor;
    private final ComponentsTransactionFactory componentsTransactionFactory;

    @Inject
    public GeneratorRepositoryProvider(InjectorProcessor injectorProcessor,
                                       AnalysisGenerationFactory analysisGenerationFactory,
                                       Provider<ActivityAnalysis> activityAnalysisProvider,
                                       Provider<BroadcastReceiverAnalysis> broadcastReceiverAnalysisProvider,
                                       Provider<ServiceAnalysis> serviceAnalysisProvider,
                                       Provider<FragmentAnalysis> fragmentAnalysisProvider,
                                       Provider<ApplicationAnalysis> applicationAnalysisProvider,
                                       AnalysisGenerationTransactionProcessorBuilderFactory processorFactory,
                                       GenerateModuleProcessor generateModuleProcessor,
                                       PackageHelperTransactionFactory packageHelperTransactionFactory,
                                       ModuleProcessorBuilder moduleProcessorBuilder,
                                       ImplementedByProcessorBuilder implementedByProcessorBuilder,
                                       TransactionProcessorPool<Map<Provider<ASTType>, JDefinedClass>, Void> componentsRepositoryProcessor,
                                       ComponentsTransactionFactory componentsTransactionFactory) {
        this.injectorProcessor = injectorProcessor;
        this.analysisGenerationFactory = analysisGenerationFactory;
        this.activityAnalysisProvider = activityAnalysisProvider;
        this.broadcastReceiverAnalysisProvider = broadcastReceiverAnalysisProvider;
        this.serviceAnalysisProvider = serviceAnalysisProvider;
        this.fragmentAnalysisProvider = fragmentAnalysisProvider;
        this.applicationAnalysisProvider = applicationAnalysisProvider;
        this.processorFactory = processorFactory;
        this.generateModuleProcessor = generateModuleProcessor;
        this.packageHelperTransactionFactory = packageHelperTransactionFactory;
        this.moduleProcessorBuilder = moduleProcessorBuilder;
        this.implementedByProcessorBuilder = implementedByProcessorBuilder;
        this.componentsRepositoryProcessor = componentsRepositoryProcessor;
        this.componentsTransactionFactory = componentsTransactionFactory;
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
            WorkerProvider workerProvider = analysisGenerationFactory.buildAnalysisGenerationProvider(providerEntry.getValue());
            AnalysisGenerationTransactionProcessorBuilder processorBuilder = processorFactory.buildBuilder(workerProvider);

            componentProcessors.add(processorBuilder.getTransactionProcessor());

            processorMapBuilder.put(providerEntry.getKey(), processorBuilder);
        }

        TransactionProcessor<Provider<ASTType>, JDefinedClass> componentsProcessor = new TransactionProcessorComposite<Provider<ASTType>, JDefinedClass>(componentProcessors.build());

        // Manifest processing (depends on components)
        TransactionProcessor<Void, Void> manifestProcessor = new TransactionProcessorPredefined(ImmutableSet.of(new Transaction<Void, Void>(generateModuleProcessor)));
        TransactionProcessor<Void, Void> componentProcessorCompletion = new TransactionProcessorChain(

                new TransactionProcessorChannel<Provider<ASTType>, JDefinedClass, Void>(componentsProcessor, componentsRepositoryProcessor, componentsTransactionFactory), manifestProcessor);

        ImmutableSet.Builder<TransactionProcessor<?, ?>> configurationDependentBuilders = ImmutableSet.builder();

        configurationDependentBuilders.add(injectorProcessor.getTransactionProcessor());
        configurationDependentBuilders.add(componentProcessorCompletion);

        processorMapBuilder.put(Injector.class, injectorProcessor);

        // Package Helper processing (to be run last)
        TransactionProcessor<Void, Void> packageHelperProcessor = new TransactionProcessorPredefined(ImmutableSet.of(packageHelperTransactionFactory.buildTransaction()));

        TransactionProcessor<?, ?> configurationDependentProcessors =
                new TransactionProcessorComposite(configurationDependentBuilders.build());

        TransactionProcessor<Void, Void> processor = new TransactionProcessorChain(configurationProcessors,
                new TransactionProcessorChain(configurationDependentProcessors, packageHelperProcessor));

        return new GeneratorRepository(processorMapBuilder.build(), processor);
    }
}
