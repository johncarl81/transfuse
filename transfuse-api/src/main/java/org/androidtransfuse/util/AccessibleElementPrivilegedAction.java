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
package org.androidtransfuse.util;

import java.lang.reflect.AccessibleObject;
import java.security.PrivilegedExceptionAction;

/**
 * Executes a PrivilegedExceptionAction against a AccessibleObject with the convenience of toggling the Accessible
 * parameter on the AccessibleObject.  This essentially turns off encapsulation via declaring methods, fields, etc as
 * private.
 *
 * @author John Ericksen
 */
public abstract class AccessibleElementPrivilegedAction<T, E extends AccessibleObject> implements PrivilegedExceptionAction<T> {

    private final E accessible;

    protected AccessibleElementPrivilegedAction(E accessible) {
        this.accessible = accessible;
    }

    @Override
    public T run() throws Exception {
        boolean previous = this.accessible.isAccessible();
        accessible.setAccessible(true);

        T output = run(accessible);

        accessible.setAccessible(previous);

        return output;
    }

    /**
     * Execute a Privileged Action against the given element which has been toggled to be accessible.
     *
     * @param element input AccessibleObject
     * @return T
     * @throws Exception
     */
    public abstract T run(E element) throws Exception;
}