package org.androidrobotics.util;

import java.lang.reflect.AccessibleObject;
import java.security.PrivilegedExceptionAction;

public abstract class AccessibleElementPrivilegedAction<T, E extends AccessibleObject> implements PrivilegedExceptionAction<T> {

    private E accessibleObject;

    protected AccessibleElementPrivilegedAction(E accessibleObject) {
        this.accessibleObject = accessibleObject;
    }

    public T run() throws Exception {
        boolean accessible = accessibleObject.isAccessible();
        accessibleObject.setAccessible(true);

        T output = run(accessibleObject);

        accessibleObject.setAccessible(accessible);

        return output;
    }

    public abstract T run(E element) throws Exception;
}