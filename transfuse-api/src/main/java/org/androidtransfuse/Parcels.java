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
package org.androidtransfuse;

import android.os.Parcelable;
import org.androidtransfuse.util.GeneratedRepositoryProxy;

/**
 * Static utility class used to wrap an {@code @Parcel} annotated class with the generated {@code Parcelable} wrapper.
 *
 * @author John Ericksen
 */
public final class Parcels {

    public static final String PARCELS_NAME = "Parcels";
    public static final String PARCELS_REPOSITORY_NAME = "Transfuse$Parcels";
    public static final String PARCELS_PACKAGE = "org.androidtransfuse";

    private static final GeneratedRepositoryProxy<ParcelRepository> PROXY =
            new GeneratedRepositoryProxy<ParcelRepository>(PARCELS_PACKAGE, PARCELS_REPOSITORY_NAME);

    private Parcels(){
        // private utility class constructor
    }

    /**
     * Testing method for replacing the Transfuse$Parcels class with one referenced in the given classloader.
     *
     * @param classLoader
     */
    protected static void update(ClassLoader classLoader){
        PROXY.update(classLoader);
    }

    /**
     * Wraps the input {@code @Parcel} annotated class with a {@code Parcelable} wrapper.
     *
     * @throws org.androidtransfuse.util.TransfuseRuntimeException if there was an error looking up the wrapped
     * Transfuse$Parcels class.
     * @param input Parcel
     * @return Parcelable wrapper
     */
    public static Parcelable wrap(Object input) {
        return PROXY.get().wrap(input);
    }

    /**
     * Proxy Interface to be implemented by code generation.
     */
    public static interface ParcelRepository {

        /**
         * Wraps the input {@code @Parcel} annotated class with a {@code Parcelable} wrapper.
         *
         * @param input Parcel
         * @return Parcelable wrapper
         */
        Parcelable wrap(Object input);
    }

    /**
     * Factory class for building a Parcelable from the given input.
     */
    public static interface ParcelableFactory<T> {

        String BUILD_PARCELABLE = "buildParcelable";

        /**
         * Build the corresponding Parcelable class.
         *
         * @param input input to wrap with a Parcelable
         * @return Parcelable instance
         */
        Parcelable buildParcelable(T input);
    }
}
