package org.androidtransfuse.analysis.targets;

import javax.inject.Inject;

public class A {
    private B b;
    private B b2;
    private static int constructionCounter = 0;

    @Inject
    public A(B b) {
        constructionCounter++;
        this.b = b;
    }

    @Inject
    public void setB2(B b2){
        this.b2 = b2;
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