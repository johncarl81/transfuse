package org.androidtransfuse.integrationTest.inject;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class LoopOne {

    @Inject
    private LoopTwo dependencyLoopTwo;

    public LoopTwo getTwo() {
        return dependencyLoopTwo;
    }
}
