package org.androidtransfuse.intentFactory;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class IntentFactory {

    private Context context;

    @Inject
    public IntentFactory(Context context) {
        this.context = context;
    }

    public void start(IntentFactoryStrategy parameters) {
        Intent intent = buildIntent(parameters);

        parameters.start(context, intent);

    }

    public Intent buildIntent(IntentFactoryStrategy parameters) {
        android.content.Intent intent = new android.content.Intent(context, parameters.getTargetContext());

        intent.putExtras(parameters.getExtras());

        return intent;
    }
}
