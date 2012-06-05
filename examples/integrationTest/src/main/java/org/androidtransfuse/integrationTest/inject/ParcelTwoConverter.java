package org.androidtransfuse.integrationTest.inject;

import org.androidtransfuse.annotations.ParcelConverter;

/**
 * @author John Ericksen
 */
public class ParcelTwoConverter implements ParcelConverter<ParcelTwo> {
    @Override
    public void toParcel(ParcelTwo input, android.os.Parcel destinationParcel) {
        destinationParcel.writeString(input.getValue());
    }

    @Override
    public ParcelTwo fromParcel(android.os.Parcel parcel) {
        ParcelTwo parcelTwo = new ParcelTwo();
        parcelTwo.setValue(parcel.readString());
        return parcelTwo;
    }
}
