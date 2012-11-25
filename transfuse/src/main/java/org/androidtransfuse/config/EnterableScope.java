package org.androidtransfuse.config;

import com.google.inject.Key;
import com.google.inject.Scope;

/**
 * Scope with enter and exit functionality.
 *
 * @author John Ericksen
 */
public interface EnterableScope extends Scope {

    /**
     * Begins the current scope.
     */
    void enter();

    /**
     * Stops the current scope.
     */
    void exit();

    /**
     * Specifies the given value to be used with in this scoping block
     *
     * @param key   representing the value
     * @param value of the object
     * @param <T>   generic parameter binding key and value
     */
    <T> void seed(Key<T> key, T value);

    /**
     * Specifies the given value to be used with in this scoping block
     *
     * @param clazz representing the value
     * @param value of the object
     * @param <T>   generic parameter binding key and value
     */
    <T> void seed(Class<T> clazz, T value);
}
