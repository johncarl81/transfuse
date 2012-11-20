package org.androidtransfuse.util;

/**
 * @author John Ericksen
 */
public interface ParcelWrapper<T> {

    String GET_PARCEL = "getParcel";

    T getParcel();
}
