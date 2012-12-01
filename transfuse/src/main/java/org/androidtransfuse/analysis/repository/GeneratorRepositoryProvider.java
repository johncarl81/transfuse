package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.analysis.*;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.gen.AnalysisGenerationFactory;
import org.androidtransfuse.processor.AnalysisGenerationTransactionProcessorBuilderFactory;
import org.androidtransfuse.processor.InjectorTransactionProcessorBuilder;
import org.androidtransfuse.util.matcher.ASTMatcherBuilder;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class GeneratorRepositoryProvider implements Provider<GeneratorRepository> {

    private final ASTMatcherBuilder astMatcherBuilder;
    private final InjectorTransactionProcessorBuilder injectorTransactionProcessorBuilder;
    private final AnalysisGenerationFactory analysisGenerationFactory;
    private final Provider<ActivityAnalysis> activityAnalysisProvider;
    private final Provider<BroadcastReceiverAnalysis> broadcastReceiverAnalysisProvider;
    private final Provider<ServiceAnalysis> serviceAnalysisProvider;
    private final Provider<FragmentAnalysis> fragmentAnalysisProvider;
    private final Provider<ApplicationAnalysis> applicationAnalysisProvider;
    private final AnalysisGenerationTransactionProcessorBuilderFactory processorFactory;

    @Inject
    public GeneratorRepositoryProvider(ASTMatcherBuilder astMatcherBuilder,
                                       InjectorTransactionProcessorBuilder injectorTransactionProcessorBuilder,
                                       AnalysisGenerationFactory analysisGenerationFactory,
                                       Provider<ActivityAnalysis> activityAnalysisProvider,
                                       Provider<BroadcastReceiverAnalysis> broadcastReceiverAnalysisProvider,
                                       Provider<ServiceAnalysis> serviceAnalysisProvider,
                                       Provider<FragmentAnalysis> fragmentAnalysisProvider,
                                       Provider<ApplicationAnalysis> applicationAnalysisProvider,
                                       AnalysisGenerationTransactionProcessorBuilderFactory processorFactory) {
        this.astMatcherBuilder = astMatcherBuilder;
        this.injectorTransactionProcessorBuilder = injectorTransactionProcessorBuilder;
        this.analysisGenerationFactory = analysisGenerationFactory;
        this.activityAnalysisProvider = activityAnalysisProvider;
        this.broadcastReceiverAnalysisProvider = broadcastReceiverAnalysisProvider;
        this.serviceAnalysisProvider = serviceAnalysisProvider;
        this.fragmentAnalysisProvider = fragmentAnalysisProvider;
        this.applicationAnalysisProvider = applicationAnalysisProvider;
        this.processorFactory = processorFactory;
    }

    @Override
    public GeneratorRepository get() {
        GeneratorRepository repository = new GeneratorRepository();


        repository.add(Injector.class, injectorTransactionProcessorBuilder);
        repository.add(Application.class,
                processorFactory.buildBuilder(
                        analysisGenerationFactory.buildAnalysisGenerationProvider(applicationAnalysisProvider)));
        repository.add(Activity.class,
                processorFactory.buildBuilder(
                        analysisGenerationFactory.buildAnalysisGenerationProvider(activityAnalysisProvider)));
        repository.add(BroadcastReceiver.class,
                processorFactory.buildBuilder(
                        analysisGenerationFactory.buildAnalysisGenerationProvider(broadcastReceiverAnalysisProvider)));
        repository.add(Service.class,
                processorFactory.buildBuilder(
                        analysisGenerationFactory.buildAnalysisGenerationProvider(serviceAnalysisProvider)));
        repository.add(Fragment.class,
                processorFactory.buildBuilder(
                        analysisGenerationFactory.buildAnalysisGenerationProvider(fragmentAnalysisProvider)));

        return repository;
    }
}
