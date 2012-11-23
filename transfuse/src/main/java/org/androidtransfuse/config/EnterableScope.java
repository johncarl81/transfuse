package org.androidtransfuse.config;

import com.google.inject.Scope;

/**
 * Scope with enter and exit functionality.
 *
 * @author John Ericksen
 */
public interface EnterableScope extends Scope {

    /**
     * Begins the current scope.
     */
    void enter();

    /**
     * Stops the current scope.
     */
    void exit();
}
