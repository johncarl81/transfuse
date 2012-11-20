package org.androidtransfuse.util;

import android.os.Parcelable;

/**
 * @author John Ericksen
 */
public interface ParcelableFactory<T> {

    String BUILD_PARCELABLE = "buildParcelable";

    Parcelable buildParcelable(T input);
}
