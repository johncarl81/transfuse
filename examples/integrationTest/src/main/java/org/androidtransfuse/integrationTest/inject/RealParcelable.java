package org.androidtransfuse.integrationTest.inject;

import android.os.Parcel;
import android.os.Parcelable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author John Ericksen
 */
public class RealParcelable implements Parcelable {

    private String value;

    @SuppressWarnings("UnusedDeclaration")
    public final static Creator<RealParcelable> CREATOR = new Creator<RealParcelable>() {

        @Override
        public RealParcelable createFromParcel(Parcel parcel) {
            return new RealParcelable(parcel);
        }

        @Override
        public RealParcelable[] newArray(int size) {
            return new RealParcelable[size];
        }
    };

    public RealParcelable(Parcel parcel) {
        this.value = parcel.readString();
    }

    public RealParcelable(String value) {
        this.value = value;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(value);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
