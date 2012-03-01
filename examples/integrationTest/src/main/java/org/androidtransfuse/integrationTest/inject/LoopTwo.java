package org.androidtransfuse.integrationTest.inject;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class LoopTwo {

    @Inject
    private LoopThree depdendencyLoopThree;

    public LoopThree getThree() {
        return depdendencyLoopThree;
    }
}
