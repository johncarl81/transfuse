package org.androidtransfuse.util;

import org.androidtransfuse.Injectors;
import org.androidtransfuse.scope.Scopes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author John Ericksen
 */
public class InjectorFactoryReflectionProxy<T> implements Injectors.InjectorFactory<T> {

    private final T instance;
    private final Constructor<T> scopesConstructor;

    public InjectorFactoryReflectionProxy(Class<T> injectorClass) {
        try {
            this.instance = injectorClass.newInstance();
            this.scopesConstructor = injectorClass.getConstructor(Scopes.class);
        } catch (InstantiationException e) {
            throw new TransfuseRuntimeException("Unable to create Injector Type", e);
        } catch (IllegalAccessException e) {
            throw new TransfuseRuntimeException("Unable to create Injector Type", e);
        } catch (NoSuchMethodException e) {
            throw new TransfuseRuntimeException("Unable to get Injector constructor", e);
        }
    }

    @Override
    public T get() {
        return instance;
    }

    @Override
    public T get(Scopes scopes) {
        try {
            return scopesConstructor.newInstance(scopes);
        } catch (InstantiationException e) {
            throw new TransfuseRuntimeException("Unable to create Injector Type", e);
        } catch (IllegalAccessException e) {
            throw new TransfuseRuntimeException("Unable to create Injector Type", e);
        } catch (InvocationTargetException e) {
            throw new TransfuseRuntimeException("Unable to create Injector Type", e);
        }
    }
}
