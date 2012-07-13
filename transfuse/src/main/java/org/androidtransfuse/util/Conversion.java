package org.androidtransfuse.util;

/**
 * Interface used to convert one object to another.  Used in conjunction with the CollectionConverterUtil for
 * collection conversion.
 *
 * @see CollectionConverterUtil
 * @author John Ericksen
 */
public interface Conversion<T, V> {

    /**
     * Convert the input type T into the output type V
     * @param t input
     * @return output
     */
    V convert(T t);
}
