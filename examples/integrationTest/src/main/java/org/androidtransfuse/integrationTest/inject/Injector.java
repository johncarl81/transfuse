package org.androidtransfuse.integrationTest.inject;

/**
 * @author John Ericksen
 */
@org.androidtransfuse.annotations.Injector
public interface Injector {

    InjectTarget getTarget();

    LoopOne getLoop();

    LoopTwo getLoopTwo();

    LoopThree getLoopThree();
}
