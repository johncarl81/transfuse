package org.androidtransfuse.scope;

import javax.inject.Provider;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author John Ericksen
 */
public class SingletonScope implements Scope {

    public static final String GET_INSTANCE = "getInstance";

    private ConcurrentHashMap<Class, Object> singletonMap = new ConcurrentHashMap<Class, Object>();

    private static final SingletonScope INSTANCE = new SingletonScope();

    private SingletonScope() {
        //private singleton constructor
    }

    public static SingletonScope getInstance() {
        return INSTANCE;
    }

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
