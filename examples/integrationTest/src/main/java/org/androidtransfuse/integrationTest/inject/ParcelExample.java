package org.androidtransfuse.integrationTest.inject;

import org.androidtransfuse.annotations.Parcel;
import org.androidtransfuse.integrationTest.SerializableValue;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author John Ericksen
 */
@Parcel
public class ParcelExample {

    private String name;
    private double value;
    private ParcelTwo innerParcel;
    private RealParcelable realParcelable;
    private SerializableValue serializableValue;
    private boolean[] booleans;

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

    public boolean[] getBooleans() {
        return booleans;
    }

    public void setBooleans(boolean[] booleans) {
        this.booleans = booleans;
    }

    public RealParcelable getRealParcelable() {
        return realParcelable;
    }

    public void setRealParcelable(RealParcelable realParcelable) {
        this.realParcelable = realParcelable;
    }

    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return "ParcelExample{" +
                "\n\tname='" + name + '\'' +
                ", \n\tvalue=" + value +
                ", \n\ttinnerParcel=" + innerParcel +
                ", \n\trealParcelable=" + realParcelable +
                ", \n\tserializableValue=" + serializableValue +
                ", \n\tbooleans=" + printBooleans() +
                '}';
    }

    private String printBooleans(){
        if(booleans == null){
            return "null";
        }
        StringBuilder builder = new StringBuilder();

        builder.append('[');
        boolean first = true;
        for(boolean b : booleans){
            if(first){
                first = false;
            }
            else{
                builder.append(',');
            }
            builder.append(b);
        }
        builder.append(']');
        return builder.toString();

    }
}
