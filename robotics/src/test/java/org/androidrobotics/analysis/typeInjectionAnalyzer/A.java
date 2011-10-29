package org.androidrobotics.analysis.typeInjectionAnalyzer;

import javax.inject.Inject;

public class A {
    private B b;

    @Inject
    public void setB(B b) {
        this.b = b;
    }
}