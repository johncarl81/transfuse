package org.androidtransfuse.util;

import java.lang.reflect.AccessibleObject;
import java.security.PrivilegedExceptionAction;

/**
 * Accessibility modifying Privileged Action
 *
 * @author John Ericksen
 */
public abstract class AccessibleElementPrivilegedAction<T, E extends AccessibleObject> implements PrivilegedExceptionAction<T> {

    private E accessible;

    protected AccessibleElementPrivilegedAction(E accessible) {
        this.accessible = accessible;
    }

    public T run() throws Exception {
        boolean previous = this.accessible.isAccessible();
        accessible.setAccessible(true);

        T output = run(accessible);

        accessible.setAccessible(previous);

        return output;
    }

    public abstract T run(E element) throws Exception;
}