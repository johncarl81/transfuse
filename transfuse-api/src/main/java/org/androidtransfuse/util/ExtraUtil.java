/**
 * Copyright 2012 John Ericksen
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
package org.androidtransfuse.util;

import android.os.Bundle;

/**
 * @author John Ericksen
 */
public final class ExtraUtil {

    public static final String GET_EXTRA = "getExtra";
    public static final String GET_INSTANCE = "getInstance";

    private static final ExtraUtil INSTANCE = new ExtraUtil();

    public static ExtraUtil getInstance() {
        return INSTANCE;
    }

    private ExtraUtil() {
        //singleton constructor
    }

    public Object getExtra(Bundle extras, String name, boolean nullable) {
        Object value = null;
        if (extras != null && extras.containsKey(name)) {
            value = extras.get(name);
        }
        if (!nullable && value == null) {
            throw new TransfuseInjectionException("Unable to access Extra " + name);
        }
        return value;
    }
}
