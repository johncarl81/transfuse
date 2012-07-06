package org.androidtransfuse.intentFactory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author John Ericksen
 */
public class ServiceIntentFactoryStrategy extends AbstractIntentFactoryStrategy {

    protected ServiceIntentFactoryStrategy(Class<? extends Context> targetContext, Bundle bundle) {
        super(targetContext, bundle);
    }

    @Override
    public void start(Context context, Intent intent) {
        context.startService(intent);
    }
}
