package org.androidtransfuse.scope;

/**
 * @author John Ericksen
 */
public interface ContextScopeHolder {

    String GET_SCOPE = "getScope";

    Scope getScope();
}
