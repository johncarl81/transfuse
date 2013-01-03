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
import org.androidtransfuse.util.ParcelRepository;

/**
 * @author John Ericksen
 */
public class Parcels {

    public static final String PARCELS_NAME = "Parcels";
    public static final String PARCELS_REPOSITORY_NAME = "Transfuse$Parcels";
    public static final String PARCELS_PACKAGE = "org.androidtransfuse";

    private static final GeneratedRepositoryProxy<ParcelRepository> proxy =
            new GeneratedRepositoryProxy<ParcelRepository>(PARCELS_PACKAGE, PARCELS_REPOSITORY_NAME);

    protected static void update(ClassLoader classLoader){
        proxy.update(classLoader);
    }

    public static Parcelable wrap(Object input) {
        ParcelRepository parcelRepository = proxy.get();
        return parcelRepository == null ? null : parcelRepository.wrap(input);
    }
}
