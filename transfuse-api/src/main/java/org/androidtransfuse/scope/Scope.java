package org.androidtransfuse.scope;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public interface Scope {

    Object getScopedObject(Class clazz, Provider provider);
}
