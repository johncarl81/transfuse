package org.androidtransfuse.scope;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public interface Scope {

    String GET_SCOPED_OBJECT = "getScopedObject";

    <T> T getScopedObject(Class<T> clazz, Provider<T> provider);
}
