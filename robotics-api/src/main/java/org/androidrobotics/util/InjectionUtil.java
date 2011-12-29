package org.androidrobotics.util;

import java.lang.reflect.Field;

/**
 * @author John Ericksen
 */
public class InjectionUtil {

    public static final String SET_FIELD_METHOD = "setField";

    public static void setField(Object target, String field, Object source, boolean superTarget) {

        try {
            Class targetClass;

            if (superTarget) {
                targetClass = target.getClass().getSuperclass();
            } else {
                targetClass = target.getClass();
            }

            Field classField = targetClass.getDeclaredField(field);
            boolean accessible = classField.isAccessible();
            classField.setAccessible(true);

            classField.set(target, source);

            classField.setAccessible(accessible);

        } catch (NoSuchFieldException e) {
            throw new RoboticsInjectionException("Exception during field injection: NoSuchFieldException", e);
        } catch (IllegalAccessException e) {
            throw new RoboticsInjectionException("Exception during field injection: IllegalAccessException", e);
        }
    }

}
