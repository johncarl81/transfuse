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

import org.androidtransfuse.integrationTest.SerializableValue;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.parceler.Parcel;

/**
 * @author John Ericksen
 */
@Parcel(Parcel.Serialization.METHOD)
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
                ", \n\tinnerParcel=" + innerParcel +
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
