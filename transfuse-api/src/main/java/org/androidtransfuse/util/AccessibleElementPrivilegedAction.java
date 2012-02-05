package org.androidtransfuse.util;

import java.lang.reflect.AccessibleObject;
import java.security.PrivilegedExceptionAction;

public abstract class AccessibleElementPrivilegedAction<T, E extends AccessibleObject> implements PrivilegedExceptionAction<T> {

    private E accessible;

    protected AccessibleElementPrivilegedAction(E accessible) {
        this.accessible = accessible;
    }

    public T run() throws Exception {
        boolean accessible = this.accessible.isAccessible();
        this.accessible.setAccessible(true);

        T output = run(this.accessible);

        this.accessible.setAccessible(accessible);

        return output;
    }

    public abstract T run(E element) throws Exception;
}