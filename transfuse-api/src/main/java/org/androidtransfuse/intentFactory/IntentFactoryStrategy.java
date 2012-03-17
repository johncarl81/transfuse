package org.androidtransfuse.intentFactory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author John Ericksen
 */
public interface IntentFactoryStrategy {

    void start(Context context, Intent intent);

    Class<? extends Context> getTargetContext();

    Bundle getExtras();
}
