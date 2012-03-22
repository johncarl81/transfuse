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
    
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SingletonDependency getSingletonDependency() {
        return singletonDependency;
    }
}
