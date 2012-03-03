package org.androidtransfuse.integrationTest.scope;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
@Singleton
public class SingletonObject {

    @Inject
    private SingletonDependency singletonDependency;

    public SingletonDependency getSingletonDependency() {
        return singletonDependency;
    }
}
