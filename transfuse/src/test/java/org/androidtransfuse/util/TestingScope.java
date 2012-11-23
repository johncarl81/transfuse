package org.androidtransfuse.util;

import com.google.inject.Key;
import com.google.inject.Provider;
import org.androidtransfuse.config.EnterableScope;

import java.util.HashMap;

public class TestingScope implements EnterableScope {

    private HashMap<Key<?>, Object> values = new HashMap<Key<?>, Object>();

    public void enter() {//noop
    }

    public void exit() {//noop
    }

    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
        return new Provider<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T get() {
                // if values is null return null otherwise return the stored value of one exists
                if (!values.containsKey(key)) {
                    T object = unscoped.get();
                    values.put(key, object);
                }
                return (T) values.get(key);
            }
        };
    }
}