package org.androidtransfuse.intentFactory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author John Ericksen
 */
public interface IntentFactoryStrategy {

    String START_METHOD = "start";
    String GET_TARGET_CONTEXT_METHOD = "getTargetContext";
    String GET_EXTRAS_METHOD = "getExtras";

    void start(Context context, Intent intent);

    Class<? extends Context> getTargetContext();

    Bundle getExtras();
}
