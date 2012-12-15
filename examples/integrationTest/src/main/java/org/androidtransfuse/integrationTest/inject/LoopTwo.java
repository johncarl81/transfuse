package org.androidtransfuse.integrationTest.inject;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class LoopTwo {

    @Inject
    private LoopThree dependencyLoopThree;

    public LoopThree getThree() {
        return dependencyLoopThree;
    }
}
