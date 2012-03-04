package org.androidtransfuse.util;

import android.os.Bundle;

/**
 * @author John Ericksen
 */
public class ExtraUtil {

    public static final String GET_EXTRA = "getExtra";

    public static Object getExtra(Bundle extras, String name, boolean nullable) {
        Object value = null;
        if (extras != null && extras.containsKey(name)) {
            value = extras.get(name);
        }
        if (!nullable && value == null) {
            throw new TransfuseInjectionException("Unable to access Extra " + name);
        }
        return value;
    }
}
