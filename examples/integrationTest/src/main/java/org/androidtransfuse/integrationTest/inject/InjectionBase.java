package org.androidtransfuse.integrationTest.inject;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class InjectionBase {

    @Inject
    private InjectTarget baseTarget;

    public InjectTarget getBaseTarget() {
        return baseTarget;
    }
}
