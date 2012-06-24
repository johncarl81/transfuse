package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.analysis.ActivityAnalysis;
import org.androidtransfuse.analysis.BroadcastReceiverAnalysis;
import org.androidtransfuse.analysis.FragmentAnalysis;
import org.androidtransfuse.analysis.ServiceAnalysis;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.BroadcastReceiver;
import org.androidtransfuse.annotations.Fragment;
import org.androidtransfuse.annotations.Service;
import org.androidtransfuse.gen.AnalysisGenerationFactory;
import org.androidtransfuse.gen.ComponentGenerator;
import org.androidtransfuse.util.matcher.ASTMatcherBuilder;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class GeneratorRepositoryProvider implements Provider<GeneratorRepository> {

    private ASTMatcherBuilder astMatcherBuilder;
    private AnalysisGenerationFactory analysisGenerationFactory;
    private ActivityAnalysis activityAnalysis;
    private ComponentGenerator componentGenerator;
    private BroadcastReceiverAnalysis broadcastReceiverAnalysis;
    private ServiceAnalysis serviceAnalysis;
    private FragmentAnalysis fragmentAnalysis;

    @Inject
    public GeneratorRepositoryProvider(ASTMatcherBuilder astMatcherBuilder,
                                       AnalysisGenerationFactory analysisGenerationFactory,
                                       ActivityAnalysis activityAnalysis,
                                       ComponentGenerator componentGenerator,
                                       BroadcastReceiverAnalysis broadcastReceiverAnalysis,
                                       ServiceAnalysis serviceAnalysis,
                                       FragmentAnalysis fragmentAnalysis) {
        this.astMatcherBuilder = astMatcherBuilder;
        this.analysisGenerationFactory = analysisGenerationFactory;
        this.activityAnalysis = activityAnalysis;
        this.componentGenerator = componentGenerator;
        this.broadcastReceiverAnalysis = broadcastReceiverAnalysis;
        this.serviceAnalysis = serviceAnalysis;
        this.fragmentAnalysis = fragmentAnalysis;
    }

    @Override
    public GeneratorRepository get() {
        GeneratorRepository repository = new GeneratorRepository();

        repository.add(astMatcherBuilder.type().annotatedWith(Activity.class).build(),
                analysisGenerationFactory.buildAnalysisGeneration(activityAnalysis, componentGenerator));
        repository.add(astMatcherBuilder.type().annotatedWith(BroadcastReceiver.class).build(),
                analysisGenerationFactory.buildAnalysisGeneration(broadcastReceiverAnalysis, componentGenerator));
        repository.add(astMatcherBuilder.type().annotatedWith(Service.class).build(),
                analysisGenerationFactory.buildAnalysisGeneration(serviceAnalysis, componentGenerator));
        repository.add(astMatcherBuilder.type().annotatedWith(Fragment.class).build(),
                analysisGenerationFactory.buildAnalysisGeneration(fragmentAnalysis, componentGenerator));

        return repository;
    }
}
