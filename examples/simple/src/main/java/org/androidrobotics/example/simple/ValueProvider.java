package org.androidrobotics.example.simple;

import android.app.Application;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ValueProvider implements Provider<ProvidedValue> {

    public static final String PROVIDED_VALUE = "Hello";

    @Inject
    private Application application;

    @Override
    public ProvidedValue get() {
        return new ProvidedValue(PROVIDED_VALUE);
    }
}
