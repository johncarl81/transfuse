package org.androidtransfuse.util;

/**
 * @author John Ericksen
 */
public interface ParcelableWrapper<T> {

    String GET_WRAPPED = "getWrapped";

    T getWrapped();
}
