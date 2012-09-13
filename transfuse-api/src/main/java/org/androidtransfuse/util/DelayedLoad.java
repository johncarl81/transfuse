package org.androidtransfuse.util;

/**
 * @author John Ericksen
 */
public interface DelayedLoad<T> {

    String LOAD_METHOD = "load";

    void load(T target);
}
