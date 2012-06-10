package org.androidtransfuse.integrationTest.inject;

import org.androidtransfuse.annotations.Parcel;

/**
 * @author John Ericksen
 */
@Parcel(ParcelTwoConverter.class)
public class ParcelTwo {

    private String value;

    public ParcelTwo() {
        //empty bean constructor
    }

    public ParcelTwo(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
