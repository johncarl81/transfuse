package org.androidrobotics.analysis.typeInjectionAnalyzer;

import javax.inject.Inject;

public class D {
    //back link
    private B b;

    @Inject
    public D(B b) {
        this.b = b;
    }
}