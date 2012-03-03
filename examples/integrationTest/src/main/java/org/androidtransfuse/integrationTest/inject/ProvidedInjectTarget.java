package org.androidtransfuse.integrationTest.inject;

/**
 * @author John Ericksen
 */
public class ProvidedInjectTarget {

    private InjectTarget injectTarget;

    public ProvidedInjectTarget(InjectTarget injectTarget) {
        this.injectTarget = injectTarget;
    }

    public InjectTarget getInjectTarget() {
        return injectTarget;
    }
}
