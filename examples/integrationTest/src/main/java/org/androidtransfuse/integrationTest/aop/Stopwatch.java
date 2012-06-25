package org.androidtransfuse.integrationTest.aop;

/**
 * @author John Ericksen
 */
public class Stopwatch {

    private long start;

    public void start() {
        start = System.currentTimeMillis();
    }

    public long stop(){
        return System.currentTimeMillis() - start;
    }
}
