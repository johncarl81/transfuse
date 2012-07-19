package org.androidtransfuse.integrationTest.inject;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
@org.androidtransfuse.annotations.Injector
public interface Injector {

    InjectTarget getTarget();

    LoopOne getLoop();

    LoopTwo getLoopTwo();

    LoopThree getLoopThree();

    Provider<LoopThree> getLoopThreeProvider();
}
