package org.androidtransfuse.util;

/**
 * @author John Ericksen
 */
public interface ParcelableWrapper<T> {

    final String GET_WRAPPED = "getWrapped";

    T getWrapped();
}
