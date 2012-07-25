package org.androidtransfuse.analysis.targets;

import javax.inject.Inject;

public class EImpl implements E {
    private F f;

    @Inject
    public EImpl(F f) {
        this.f = f;
    }
}