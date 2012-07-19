package org.androidtransfuse.integrationTest;

import org.androidtransfuse.integrationTest.inject.Injector;
import org.androidtransfuse.integrationTest.inject.InjectorImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public final class InjectorFinder {

    private static final Map<Class<?>, Object> injectorProviders = new HashMap<Class<?>, Object>();

    static {
        injectorProviders.put(Injector.class, new InjectorImpl());
    }

    public static <T> T create(Class<T> injectorClass) {
        if(injectorProviders.containsKey(injectorClass)){
            return (T) injectorProviders.get(injectorClass);
        }
        return null;
    }
}
