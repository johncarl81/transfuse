package org.androidtransfuse.example.simple;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ProviderTest implements Provider<ProviderTestValue> {

    @Inject
    private AnotherValue anotherValue;

    @Override
    public ProviderTestValue get() {
        return new ProviderTestValueImpl();
    }
}
