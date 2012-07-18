package org.androidtransfuse.analysis.targets;

import javax.inject.Inject;

public class A {
    private B b;
    private static int constructionCounter = 0;

    @Inject
    public A(B b) {
        constructionCounter++;
        this.b = b;
    }

    public B getB() {
        return b;
    }

    public static int getConstructionCounter() {
        return constructionCounter;
    }

    public static void reset(){
        constructionCounter = 0;
    }
}