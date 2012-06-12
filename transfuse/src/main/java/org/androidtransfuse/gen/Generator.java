package org.androidtransfuse.gen;

/**
 * @author John Ericksen
 */
public interface Generator<T> {

    void generate(T descriptor);
}
