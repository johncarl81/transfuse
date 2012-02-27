package org.androidtransfuse.util;

import android.os.Bundle;

/**
 * @author John Ericksen
 */
public class ExtraUtil {

    public static final String GET_EXTRA = "getExtra";

    public static Object getExtra(Bundle extras, String name, boolean nullable) {
        if (nullable && extras != null && extras.containsKey(name)) {
            return extras.get(name);
        }
        if (!nullable && (extras == null || !extras.containsKey(name))) {
            throw new TransfuseInjectionException("Unable to access Extra " + name);
        }
        return null;
    }
}
