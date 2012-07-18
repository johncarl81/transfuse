package org.androidtransfuse.analysis.targets;

import javax.inject.Inject;
import javax.inject.Provider;

public class FProvider implements Provider<F> {

    private D d;

    @Inject
    public FProvider(D d) {
        this.d = d;
    }

    @Override
    public F get() {
        return new F(d);
    }
}