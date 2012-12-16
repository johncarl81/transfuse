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
package org.androidtransfuse.integrationTest;

import java.lang.reflect.Field;

/**
 * @author John Ericksen
 */
public class DelegateUtil {

    public static <T> T getDelegate(Object root, Class<T> delegateClass) {
        try{
            Field delegateField = findDelegateField(root.getClass(), delegateClass);

            delegateField.setAccessible(true);
            T delegate = (T) delegateField.get(root);
            delegateField.setAccessible(false);

            return delegate;
        }
        catch (IllegalAccessException e){
            throw new TransfuseTestException("Illegal access to field", e);
        }
    }


    private static Field findDelegateField(Class target, Class type) {
        Field delegateField = null;

        for (Field field : target.getDeclaredFields()) {
            if (type.isAssignableFrom(field.getType())) {
                if (delegateField != null) {
                    throw new TransfuseTestException("Type found more than once");
                }
                delegateField = field;
            }
        }

        return delegateField;
    }
}
