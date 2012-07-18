package org.androidtransfuse.analysis.targets;

import javax.inject.Inject;

public class BImpl implements B {
    private C c;
    private static int constructionCounter = 0;

    @Inject
    public BImpl(C c) {
        constructionCounter++;
        this.c = c;
    }

    public C getC() {
        return c;
    }

    public static int getConstructionCounter() {
        return constructionCounter;
    }

    public static void reset(){
        constructionCounter = 0;
    }
}