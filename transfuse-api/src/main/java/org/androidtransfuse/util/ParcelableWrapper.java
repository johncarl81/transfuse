package org.androidtransfuse.util;

/**
 * @author John Ericksen
 */
public interface ParcelableWrapper<T> {

    public static final String GET_WRAPPED = "getWrapped";

    public T getWrapped();
}
