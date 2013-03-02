package org.androidtransfuse.bootstrap;

import org.androidtransfuse.util.TransfuseRuntimeException;

import java.lang.annotation.Annotation;

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
    public <S> Bootstraps.BootstrapInjector<T> add(Class<? extends Annotation> annotation, Class<S> singletonClass, S singleton) {
        return injector.add(annotation, singletonClass, singleton);
    }

    @Override
    public void inject(T input) {
        injector.inject(input);
    }
}
