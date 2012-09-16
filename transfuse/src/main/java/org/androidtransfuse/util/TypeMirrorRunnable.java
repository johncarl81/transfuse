package org.androidtransfuse.util;

/**
 * @author John Ericksen
 */
public abstract class TypeMirrorRunnable<T> implements Runnable{

    private final T annotation;

    protected TypeMirrorRunnable(T annotation) {
        this.annotation = annotation;
    }

    @Override
    public void run() {
        run(annotation);
    }

    public abstract void run(T annotation);
}
