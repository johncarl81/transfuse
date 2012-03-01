package org.androidtransfuse.integrationTest.inject;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class LoopOne {

    @Inject
    private LoopTwo depedencyLoopTwo;

    public LoopTwo getTwo() {
        return depedencyLoopTwo;
    }
}
