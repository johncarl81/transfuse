package org.androidtransfuse.integrationTest.inject;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class LoopThreeImpl implements LoopThree {

    @Inject
    private LoopOne dependencyLoopOne;

    public LoopOne getOne() {
        return dependencyLoopOne;
    }
}
