package org.androidtransfuse.scope;

import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class SingletonScope implements Scope {

    private Map<Class, Object> singletonMap = new HashMap<Class, Object>();

    @Override
    public Object getScopedObject(Class clazz, Provider provider) {
        Object result = singletonMap.get(clazz);
        if (result == null) {
            synchronized (this) {
                result = singletonMap.get(clazz);
                if (result == null) {
                    result = provider.get();
                    singletonMap.put(clazz, result);
                }
            }
        }

        return result;
    }
}
