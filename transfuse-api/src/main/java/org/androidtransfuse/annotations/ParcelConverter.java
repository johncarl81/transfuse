package org.androidtransfuse.annotations;

/**
 * @author John Ericksen
 */
public interface ParcelConverter<T> {

    String TRANSLATE_METHOD = "translate";

    void translate(T input, android.os.Parcel destinationParcel);

    T translate(android.os.Parcel parcel);

    public static final class EmptyConverter implements ParcelConverter<Object> {
        @Override
        public void translate(Object input, android.os.Parcel destinationParcel) {
        }

        @Override
        public Object translate(android.os.Parcel parcel) {
            return null;
        }
    }
}
