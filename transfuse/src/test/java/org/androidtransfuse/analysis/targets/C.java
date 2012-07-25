package org.androidtransfuse.analysis.targets;

import javax.inject.Inject;

public class C {
    private A a;
    private static int constructionCounter = 0;

    @Inject
    public C(A a) {
        constructionCounter++;
        this.a = a;
    }

    public A getA() {
        return a;
    }

    public static int getConstructionCounter() {
        return constructionCounter;
    }

    public static void reset(){
        constructionCounter = 0;
    }
}