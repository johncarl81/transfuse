package org.androidtransfuse.config;

import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;

/**
 * Toxic Provider that throws an exception if get() is called.
 * <p/>
 * Should be used as a placeholder to ensure that a scoped object is not created by this provider, but instead seeded.
 *
 * @author John Ericksen
 */
public class ThrowingProvider<T> implements Provider<T> {

    @Override
    public T get() {
        throw new OutOfScopeException("Expected seeded object, unable to construct directly.");
    }
}
