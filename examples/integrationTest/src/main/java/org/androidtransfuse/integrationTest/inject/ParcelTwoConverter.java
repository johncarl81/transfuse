/**
 * Copyright 2011-2015 John Ericksen
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

import org.parceler.ParcelConverter;

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
