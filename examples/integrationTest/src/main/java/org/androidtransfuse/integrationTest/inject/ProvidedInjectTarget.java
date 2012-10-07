package org.androidtransfuse.integrationTest.inject;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ProvidedInjectTarget {

    private InjectTarget injectTarget;

    @Inject
    protected ProvidedInjectTarget(InjectTarget injectTarget) {
        this.injectTarget = injectTarget;
    }

    public InjectTarget getInjectTarget() {
        return injectTarget;
    }
}
