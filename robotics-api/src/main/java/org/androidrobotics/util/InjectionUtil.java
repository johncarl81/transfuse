package org.androidrobotics.util;

import java.lang.reflect.Field;

/**
 * @author John Ericksen
 */
public class InjectionUtil {

    public static final String SET_FIELD_METHOD = "setField";
    public static final String SET_SUPER_METHOD = "setSuperField";

    public static void setField(Object target, String field, Object source) {
        setTargetField(target.getClass(), target, field, source);
    }

    public static void setSuperField(Object target, String field, Object source) {
        setTargetField(target.getClass().getSuperclass(), target, field, source);
    }

    private static void setTargetField(Class targetClass, Object target, String field, Object source) {
        try {
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
