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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof B)) return false;

        B b = (B) o;

        return !(c != null ? !c.equals(b.getC()) : b.getC() != null);
    }

    @Override
    public int hashCode() {
        return c != null ? c.hashCode() : 0;
    }
}