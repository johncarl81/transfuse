package org.androidtransfuse.integrationTest.layout;

import javax.inject.Provider;
import java.util.Random;

/**
 * @author John Ericksen
 */
public class RandomProvider implements Provider<Random> {
    @Override
    public Random get() {
        return new Random(System.currentTimeMillis());
    }
}
