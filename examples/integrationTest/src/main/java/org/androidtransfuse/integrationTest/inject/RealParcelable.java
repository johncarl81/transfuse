/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.integrationTest.inject;

import android.os.Parcel;
import android.os.Parcelable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author John Ericksen
 */
public class RealParcelable implements Parcelable {

    @SuppressWarnings("UnusedDeclaration")
    public static final Creator<RealParcelable> CREATOR = new Creator<RealParcelable>() {

        @Override
        public RealParcelable createFromParcel(Parcel parcel) {
            return new RealParcelable(parcel);
        }

        @Override
        public RealParcelable[] newArray(int size) {
            return new RealParcelable[size];
        }
    };

    private String value;

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

    @Override
    public String toString() {
        return "RP{" +
                "value='" + value + '\'' +
                '}';
    }
}
