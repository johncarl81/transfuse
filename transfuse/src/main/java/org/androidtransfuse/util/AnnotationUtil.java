package org.androidtransfuse.util;

import org.apache.commons.lang.StringUtils;

/**
 * Common utility methods for dealing with annotations
 *
 * @author John Ericksen
 */
public final class AnnotationUtil {

    private AnnotationUtil(){
        //noop utility class constructor
    }

    public static <T> T checkDefault(T input, T defaultValue) {
        if (input.equals(defaultValue)) {
            return null;
        }
        return input;
    }

    public static String checkBlank(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        return input;
    }
}
