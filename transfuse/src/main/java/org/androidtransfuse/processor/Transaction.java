package org.androidtransfuse.processor;

/**
 * @author John Ericksen
 */
public interface Transaction<V, R> extends Runnable {

    boolean isComplete();

    V getValue();

    R getResult();
}
