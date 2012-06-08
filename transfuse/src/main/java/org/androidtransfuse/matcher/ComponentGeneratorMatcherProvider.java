package org.androidtransfuse.matcher;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.BroadcastReceiver;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ComponentGeneratorMatcherProvider implements Provider<Matcher> {

    private ASTMatcherBuilder astMatcherBuilder;
    private Provider<ActivityMatchExecution> activityMatchExecutionProvider;
    private Provider<BroadcastReceiverMatchExecution> broadcastReceiverMatchExecutionProvider;

    @Inject
    public ComponentGeneratorMatcherProvider(ASTMatcherBuilder astMatcherBuilder,
                                             Provider<BroadcastReceiverMatchExecution> broadcastReceiverMatchExecutionProvider,
                                             Provider<ActivityMatchExecution> activityMatchExecutionProvider) {
        this.astMatcherBuilder = astMatcherBuilder;
        this.broadcastReceiverMatchExecutionProvider = broadcastReceiverMatchExecutionProvider;
        this.activityMatchExecutionProvider = activityMatchExecutionProvider;
    }

    @Override
    public Matcher get() {
        Matcher matcher = new Matcher();

        matcher.addMatcher(astMatcherBuilder.type().annotatedWith(Activity.class).build(),
                activityMatchExecutionProvider.get());
        matcher.addMatcher(astMatcherBuilder.type().annotatedWith(BroadcastReceiver.class).build(),
                broadcastReceiverMatchExecutionProvider.get());

        return matcher;
    }
}
