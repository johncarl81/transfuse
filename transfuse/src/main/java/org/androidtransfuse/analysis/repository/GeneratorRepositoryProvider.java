package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.BroadcastReceiver;
import org.androidtransfuse.gen.ActivityGenerator;
import org.androidtransfuse.gen.BroadcastReceiverGenerator;
import org.androidtransfuse.util.matcher.ASTMatcherBuilder;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class GeneratorRepositoryProvider implements Provider<GeneratorRepository> {

    private ASTMatcherBuilder astMatcherBuilder;
    private Provider<ActivityGenerator> activityMatchExecutionProvider;
    private Provider<BroadcastReceiverGenerator> broadcastReceiverMatchExecutionProvider;

    @Inject
    public GeneratorRepositoryProvider(ASTMatcherBuilder astMatcherBuilder,
                                       Provider<BroadcastReceiverGenerator> broadcastReceiverMatchExecutionProvider,
                                       Provider<ActivityGenerator> activityMatchExecutionProvider) {
        this.astMatcherBuilder = astMatcherBuilder;
        this.broadcastReceiverMatchExecutionProvider = broadcastReceiverMatchExecutionProvider;
        this.activityMatchExecutionProvider = activityMatchExecutionProvider;
    }

    @Override
    public GeneratorRepository get() {
        GeneratorRepository repository = new GeneratorRepository();

        repository.add(astMatcherBuilder.type().annotatedWith(Activity.class).build(),
                activityMatchExecutionProvider.get());
        repository.add(astMatcherBuilder.type().annotatedWith(BroadcastReceiver.class).build(),
                broadcastReceiverMatchExecutionProvider.get());

        return repository;
    }
}
