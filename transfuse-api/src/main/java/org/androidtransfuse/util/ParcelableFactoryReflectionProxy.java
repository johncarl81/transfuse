package org.androidtransfuse.util;

import android.os.Parcelable;
import org.androidtransfuse.Parcels;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author John Ericksen
 */
public class ParcelableFactoryReflectionProxy<T> implements Parcels.ParcelableFactory<T> {

    private final Constructor<? extends Parcelable> constructor;

    public ParcelableFactoryReflectionProxy(Class<T> parcelClass, Class<? extends Parcelable> parcelWrapperClass) {
        try {
            this.constructor = parcelWrapperClass.getConstructor(parcelClass);
        } catch (NoSuchMethodException e) {
            throw new TransfuseRuntimeException("Unable to create ParcelFactory Type", e);
        }
    }

    @Override
    public Parcelable buildParcelable(T input) {
        try {
            return constructor.newInstance(input);
        } catch (InstantiationException e) {
            throw new TransfuseRuntimeException("Unable to create ParcelFactory Type", e);
        } catch (IllegalAccessException e) {
            throw new TransfuseRuntimeException("Unable to create ParcelFactory Type", e);
        } catch (InvocationTargetException e) {
            throw new TransfuseRuntimeException("Unable to create ParcelFactory Type", e);
        }
    }
}
