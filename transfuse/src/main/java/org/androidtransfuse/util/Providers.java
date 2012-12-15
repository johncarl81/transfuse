package org.androidtransfuse.util;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class Providers {

    public static final String OF_METHOD = "of";

    public static <T> Provider<T> of(T instance){
        return new InstanceProvider<T>(instance);
    }

    private static final class InstanceProvider<T> implements Provider<T>{

        private T instance;

        private InstanceProvider(T instance) {
            this.instance = instance;
        }

        @Override
        public T get() {
            return instance;
        }
    }
}
