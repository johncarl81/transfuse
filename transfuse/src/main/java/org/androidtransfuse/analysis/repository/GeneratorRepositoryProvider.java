package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.analysis.repository.GeneratorRepository;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.BroadcastReceiver;
import org.androidtransfuse.matcher.ASTMatcherBuilder;
import org.androidtransfuse.matcher.ActivityMatchExecution;
import org.androidtransfuse.matcher.BroadcastReceiverMatchExecution;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class GeneratorRepositoryProvider implements Provider<GeneratorRepository> {

    private ASTMatcherBuilder astMatcherBuilder;
    private Provider<ActivityMatchExecution> activityMatchExecutionProvider;
    private Provider<BroadcastReceiverMatchExecution> broadcastReceiverMatchExecutionProvider;

    @Inject
    public GeneratorRepositoryProvider(ASTMatcherBuilder astMatcherBuilder,
                                       Provider<BroadcastReceiverMatchExecution> broadcastReceiverMatchExecutionProvider,
                                       Provider<ActivityMatchExecution> activityMatchExecutionProvider) {
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
