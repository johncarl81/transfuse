package org.androidtransfuse.scope;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public interface Scope {

    <T> T getScopedObject(Class<T> clazz, Provider<T> provider);
}
