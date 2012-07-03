package org.androidtransfuse.intentFactory;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class IntentFactory {

    private Context context;
    private IntentAdapterFactory intentMockFactory;

    protected interface IntentAdapterFactory {
        Intent buildIntent(Context context, Class<? extends Context> clazz);
    }

    private static final class IntentAdapterFactoryImpl implements IntentAdapterFactory{
        @Override
        public Intent buildIntent(Context context, Class<? extends Context> clazz) {
            return new android.content.Intent(context, clazz);
        }
    }

    @Inject
    public IntentFactory(Context context) {
        this(context, new IntentAdapterFactoryImpl());
    }

    /**
     * Constructor used for testing purposes.
     *
     * @param context
     * @param intentMockFactory
     */
    protected IntentFactory(Context context, IntentAdapterFactory intentMockFactory){
        this.context = context;
        this.intentMockFactory = intentMockFactory;
    }

    public void start(IntentFactoryStrategy parameters) {
        Intent intent = buildIntent(parameters);

        parameters.start(context, intent);

    }

    public Intent buildIntent(IntentFactoryStrategy parameters) {
        android.content.Intent intent = intentMockFactory.buildIntent(context, parameters.getTargetContext());

        intent.putExtras(parameters.getExtras());

        return intent;
    }

    public PendingIntent buildPendingIntent(IntentFactoryStrategy parameters){
        return PendingIntent.getActivity(context, 0, buildIntent(parameters), 0);
    }
}
