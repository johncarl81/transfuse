package org.androidrobotics.gen;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class FactoryHolder {

    private Map<FactoryDescriptor, Object> factories = new HashMap<FactoryDescriptor, Object>();

    public void putFactory(FactoryDescriptor descriptor, Object factory) {
        factories.put(descriptor, factory);
    }

    public Object getFactory(FactoryDescriptor descriptor) {
        return factories.get(descriptor);
    }

}
