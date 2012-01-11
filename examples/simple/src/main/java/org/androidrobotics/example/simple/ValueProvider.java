package org.androidrobotics.example.simple;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ValueProvider implements Provider<ProvidedValue> {

    public static final String PROVIDED_VALUE = "Hello";

    @Override
    public ProvidedValue get() {
        return new ProvidedValue(PROVIDED_VALUE);
    }
}
