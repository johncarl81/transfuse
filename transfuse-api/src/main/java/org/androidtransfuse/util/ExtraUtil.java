package org.androidtransfuse.util;

import android.os.Bundle;

/**
 * @author John Ericksen
 */
public final class ExtraUtil {

    public static final String GET_EXTRA = "getExtra";
    public static final String GET_INSTANCE = "getInstance";

    private static final ExtraUtil INSTANCE = new ExtraUtil();

    public static ExtraUtil getInstance() {
        return INSTANCE;
    }

    private ExtraUtil() {
        //singleton constructor
    }

    public Object getExtra(Bundle extras, String name, boolean nullable) {
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
