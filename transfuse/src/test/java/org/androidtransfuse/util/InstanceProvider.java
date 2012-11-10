package org.androidtransfuse.util;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class InstanceProvider<T> implements Provider<T> {

    private final T instance;

    public InstanceProvider(T instance) {
        this.instance = instance;
    }

    @Override
    public T get() {
        return instance;
    }
}
