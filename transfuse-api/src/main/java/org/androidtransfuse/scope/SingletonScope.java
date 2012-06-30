package org.androidtransfuse.scope;

/**
 * @author John Ericksen
 */
public final class SingletonScope {

    public static final String GET_INSTANCE = "getInstance";

    private static final Scope INSTANCE = new ConcurrentDoubleLockingScope();

    private SingletonScope() {
        //private singleton constructor
    }

    public static Scope getInstance() {
        return INSTANCE;
    }
}
