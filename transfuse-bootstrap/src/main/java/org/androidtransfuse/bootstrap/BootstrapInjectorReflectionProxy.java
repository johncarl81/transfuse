package org.androidtransfuse.bootstrap;

import org.androidtransfuse.util.TransfuseRuntimeException;

/**
 * @author John Ericksen
 */
public class BootstrapInjectorReflectionProxy<T> implements Bootstraps.BootstrapInjector<T> {

    private Bootstraps.BootstrapInjector<T> injector;

    public BootstrapInjectorReflectionProxy(Class<Bootstraps.BootstrapInjector<T>> injectorClass) {

        try {
            injector = injectorClass.newInstance();
        } catch (InstantiationException e) {
            throw new TransfuseRuntimeException("Unable to create Boostrap Type", e);
        } catch (IllegalAccessException e) {
            throw new TransfuseRuntimeException("Unable to create Boostrap Type", e);
        }
    }

    @Override
    public <S> Bootstraps.BootstrapInjector<T> addSingleton(Class<S> singletonClass, S singleton) {
        return injector.addSingleton(singletonClass, singleton);
    }

    @Override
    public void inject(T input) {
        injector.inject(input);
    }
}
