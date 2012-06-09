package org.androidtransfuse.annotations;

/**
 * @author John Ericksen
 */
public interface ParcelConverter<T> {

    String CONVERT_TO_PARCEL = "toParcel";
    String CONVERT_FROM_PARCEL = "fromParcel";

    void toParcel(T input, android.os.Parcel destinationParcel);

    T fromParcel(android.os.Parcel parcel);

    class EmptyConverter implements ParcelConverter<Object> {
        @Override
        public void toParcel(Object input, android.os.Parcel destinationParcel) {
        }

        @Override
        public Object fromParcel(android.os.Parcel parcel) {
            return null;
        }
    }
}
