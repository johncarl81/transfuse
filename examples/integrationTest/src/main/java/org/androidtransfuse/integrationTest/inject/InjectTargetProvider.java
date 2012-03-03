package org.androidtransfuse.integrationTest.inject;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class InjectTargetProvider implements Provider<ProvidedInjectTarget> {

    @Inject
    private InjectTarget injectTarget;

    @Override
    public ProvidedInjectTarget get() {
        return new ProvidedInjectTarget(injectTarget);
    }
}
