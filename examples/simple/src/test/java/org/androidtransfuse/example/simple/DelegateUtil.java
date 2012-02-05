package org.androidtransfuse.example.simple;

import java.lang.reflect.Field;

/**
 * @author John Ericksen
 */
public class DelegateUtil {

    public static <T, S> T getDelegate(S root, Class<T> delegateClass) throws IllegalAccessException {
        Field delegateField = findDelegateField(root.getClass(), delegateClass);

        delegateField.setAccessible(true);
        T delegate = (T) delegateField.get(root);
        delegateField.setAccessible(false);

        return delegate;
    }


    private static Field findDelegateField(Class target, Class type) {
        Field delegateField = null;

        for (Field field : target.getDeclaredFields()) {
            if (type.isAssignableFrom(field.getType())) {
                if (delegateField != null) {
                    throw new TransfuseTestException("Type found more than once");
                }
                delegateField = field;
            }
        }

        return delegateField;
    }
}
