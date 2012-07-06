package org.androidtransfuse.intentFactory;

import android.content.Context;
import android.os.Bundle;

/**
 * @author John Ericksen
 */
public abstract class AbstractIntentFactoryStrategy implements IntentFactoryStrategy{

    private Class<? extends Context> targetContext;
    private Bundle bundle;

    protected AbstractIntentFactoryStrategy(Class<? extends Context> targetContext, Bundle bundle) {
        this.targetContext = targetContext;
        this.bundle = bundle;
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
