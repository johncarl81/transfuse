package org.androidtransfuse.scope;

import org.androidtransfuse.annotations.TransfuseModule;

import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
public class ScopesUtil {
    private final static Scopes INSTANCE = new Scopes()
            .addScope(ApplicationScope.ApplicationScopeQualifier.class, new ApplicationScope())
            .addScope(Singleton.class, new org.androidtransfuse.scope.ConcurrentDoubleLockingScope())
            .addScope(TransfuseModule.class, new org.androidtransfuse.scope.ConcurrentDoubleLockingScope());

    private ScopesUtil() {
    }

    public static Scopes getInstance() {
        return INSTANCE;
    }

}
