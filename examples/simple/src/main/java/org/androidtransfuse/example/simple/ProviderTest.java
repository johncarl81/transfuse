package org.androidtransfuse.example.simple;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ProviderTest implements Provider<ProviderTestValue> {

    @Override
    public ProviderTestValue get() {
        return new ProviderTestValueImpl();
    }
}
