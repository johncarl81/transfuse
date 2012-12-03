package org.androidtransfuse.analysis.repository;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.analysis.*;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.AnalysisGenerationFactory;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.processor.*;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
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

    @Inject
    public GeneratorRepositoryProvider(InjectorProcessor injectorProcessor,
                                       AnalysisGenerationFactory analysisGenerationFactory,
                                       Provider<ActivityAnalysis> activityAnalysisProvider,
                                       Provider<BroadcastReceiverAnalysis> broadcastReceiverAnalysisProvider,
                                       Provider<ServiceAnalysis> serviceAnalysisProvider,
                                       Provider<FragmentAnalysis> fragmentAnalysisProvider,
                                       Provider<ApplicationAnalysis> applicationAnalysisProvider,
                                       AnalysisGenerationTransactionProcessorBuilderFactory processorFactory,
                                       GenerateModuleProcessor generateModuleProcessor) {
        this.injectorProcessor = injectorProcessor;
        this.analysisGenerationFactory = analysisGenerationFactory;
        this.activityAnalysisProvider = activityAnalysisProvider;
        this.broadcastReceiverAnalysisProvider = broadcastReceiverAnalysisProvider;
        this.serviceAnalysisProvider = serviceAnalysisProvider;
        this.fragmentAnalysisProvider = fragmentAnalysisProvider;
        this.applicationAnalysisProvider = applicationAnalysisProvider;
        this.processorFactory = processorFactory;
        this.generateModuleProcessor = generateModuleProcessor;
    }

    @Override
    public GeneratorRepository get() {

        ImmutableMap.Builder<Class<? extends Annotation>, TransactionProcessorBuilder> processorMapBuilder = ImmutableMap.builder();
        ImmutableSet.Builder<TransactionProcessor> componentProcessors = ImmutableSet.builder();

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

        TransactionProcessor componentsProcessor = new TransactionProcessorComposite(componentProcessors.build());
        TransactionProcessor manifestProcessor = new TransactionProcessorPredefined<Void, Void>(ImmutableSet.of(new Transaction<Void, Void>(generateModuleProcessor)));
        TransactionProcessor componentProcessorCompletion = new TransactionProcessorChain(componentsProcessor, manifestProcessor);

        ImmutableSet.Builder<TransactionProcessor> configurationDependentBuilders = ImmutableSet.builder();

        configurationDependentBuilders.add(injectorProcessor.getTransactionProcessor());
        configurationDependentBuilders.add(componentProcessorCompletion);

        processorMapBuilder.put(Injector.class, injectorProcessor);

        TransactionProcessorComposite processor = new TransactionProcessorComposite(configurationDependentBuilders.build());

        return new GeneratorRepository(processorMapBuilder.build(), processor);
    }
}
