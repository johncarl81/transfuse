package org.androidtransfuse.integrationTest;

import org.androidtransfuse.integrationTest.inject.Injector;
import org.androidtransfuse.integrationTest.inject.InjectorImpl;

/**
 * @author John Ericksen
 */
public final class InjectorFinder {

    private static final InjectorFinder INSTANCE = new InjectorFinder();

    private InjectorFinder() {
    }

    public static <T> T create(Class<T> injectorClass) {
        if (injectorClass.equals(Injector.class)) {
            return (T) new InjectorImpl();
        }
        return null;
    }
}
