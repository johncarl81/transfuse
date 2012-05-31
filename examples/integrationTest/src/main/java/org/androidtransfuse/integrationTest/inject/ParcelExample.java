package org.androidtransfuse.integrationTest.inject;

import org.androidtransfuse.annotations.Parcel;
import org.androidtransfuse.integrationTest.SerializableValue;

/**
 * @author John Ericksen
 */
@Parcel
public class ParcelExample {

    private String name;
    private double value;
    private ParcelTwo innerParcel;
    private SerializableValue serializableValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ParcelTwo getInnerParcel() {
        return innerParcel;
    }

    public void setInnerParcel(ParcelTwo innerParcel) {
        this.innerParcel = innerParcel;
    }

    public SerializableValue getSerializableValue() {
        return serializableValue;
    }

    public void setSerializableValue(SerializableValue serializableValue) {
        this.serializableValue = serializableValue;
    }
}
