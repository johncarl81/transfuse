package org.androidtransfuse.integrationTest.scope;

import org.androidtransfuse.config.OutOfScopeException;
import org.androidtransfuse.scope.Scope;
import org.androidtransfuse.scope.ScopeKey;

import javax.inject.Provider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapScope implements Scope {

    private ConcurrentMap<ScopeKey<?>, Object> values;

    public void enter() {
        values = new ConcurrentHashMap<ScopeKey<?>, Object>();
    }

    public void exit() {
        values = null;
    }

    public <T> void seed(ScopeKey<T> key, T value) {
        Map<ScopeKey<?>, Object> scopedObjects = getScopedObjectMap(key);
        scopedObjects.put(key, value);
    }

    @Override
    public <T> T getScopedObject(final ScopeKey<T> key, final Provider<T> provider) {
        ConcurrentMap<ScopeKey<?>, Object> scopedObjects = getScopedObjectMap(key);

        @SuppressWarnings("unchecked")
        Object current = scopedObjects.get(key);
        if (current == null) {
            Object value = provider.get();
            current = scopedObjects.putIfAbsent(key, value);
            if(current == null){
                current = value;
            }
        }
        return (T) current;
    }

    private <T> ConcurrentMap<ScopeKey<?>, Object> getScopedObjectMap(ScopeKey<T> key) {
        if (values == null) {
            throw new OutOfScopeException("Cannot access " + key + " outside of a scoping block");
        }
        return values;
    }
}