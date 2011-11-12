package org.androidrobotics.util;

import java.lang.reflect.Field;

/**
 * @author John Ericksen
 */
public class InjectionUtil {

    public static void setField(Object target, String field, Object source) {

        try {
            Class targetClass = target.getClass();

            Field classField = targetClass.getDeclaredField(field);
            boolean accessible = classField.isAccessible();
            classField.setAccessible(true);

            classField.set(target, source);

            classField.setAccessible(accessible);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
