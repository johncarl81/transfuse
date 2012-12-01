package org.androidtransfuse.config;

import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class MapScope implements EnterableScope {

    private Map<Key<?>, Object> values;

    public void enter() {
        values = new HashMap<Key<?>, Object>();
    }

    public void exit() {
        values = null;
    }

    public <T> void seed(Key<T> key, T value) {
        Map<Key<?>, Object> scopedObjects = getScopedObjectMap(key);
        scopedObjects.put(key, value);
    }

    public <T> void seed(Class<T> clazz, T value) {
        seed(Key.get(clazz), value);
    }

    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
        return new Provider<T>() {
            public T get() {
                Map<Key<?>, Object> scopedObjects = getScopedObjectMap(key);

                @SuppressWarnings("unchecked")
                T current = (T) scopedObjects.get(key);
                if (current == null && !scopedObjects.containsKey(key)) {
                    current = unscoped.get();
                    scopedObjects.put(key, current);
                }
                return current;
            }
        };
    }

    private <T> Map<Key<?>, Object> getScopedObjectMap(Key<T> key) {
        if (values == null) {
            throw new OutOfScopeException("Cannot access " + key + " outside of a scoping block");
        }
        return values;
    }
}
