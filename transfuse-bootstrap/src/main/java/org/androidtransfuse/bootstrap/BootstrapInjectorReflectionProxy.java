package org.androidtransfuse.bootstrap;

import org.androidtransfuse.scope.Scopes;
import org.androidtransfuse.util.TransfuseRuntimeException;

/**
 * @author John Ericksen
 */
public class BootstrapInjectorReflectionProxy<T> extends Bootstraps.BootstrapInjector<T> {

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
    protected void scopeSingletons(Scopes scopes) {
        injector.scopeSingletons(scopes);
    }

    @Override
    public void inject(Class<T> key, T input) {
        injector.inject(key, input);
    }
}
