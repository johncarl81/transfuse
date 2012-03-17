package org.androidtransfuse.intentFactory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author John Ericksen
 */
public class ActivityIntentFactoryStrategy implements IntentFactoryStrategy {

    private Class<? extends Context> targetContext;
    private Bundle bundle;

    protected ActivityIntentFactoryStrategy(Class<? extends Context> targetContext, Bundle bundle) {
        this.targetContext = targetContext;
        this.bundle = bundle;
    }

    @Override
    public void start(Context context, Intent intent) {
        context.startActivity(intent);
    }

    @Override
    public Class<? extends Context> getTargetContext() {
        return targetContext;
    }

    @Override
    public Bundle getExtras() {
        return bundle;
    }
}
