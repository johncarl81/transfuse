package org.androidtransfuse.scope;

import javax.inject.Provider;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author John Ericksen
 */
public class ConcurrentDoubleLockingScope implements Scope {

    private final ConcurrentMap<Class, Object> singletonMap = new ConcurrentHashMap<Class, Object>();

    @Override
    public <T> T getScopedObject(Class<T> clazz, Provider<T> provider) {
        Object result = singletonMap.get(clazz);
        if (result == null) {
            Object value = provider.get();
            result = singletonMap.putIfAbsent(clazz, value);
            if (result == null) {
                result = value;
            }
        }

        return (T) result;
    }
}
