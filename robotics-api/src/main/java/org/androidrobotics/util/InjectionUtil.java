package org.androidrobotics.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author John Ericksen
 */
public class InjectionUtil {

    public static final String SET_FIELD_METHOD = "setField";
    public static final String SET_METHOD_METHOD = "setMethod";
    public static final String SET_CONSTRUCTOR_METHOD = "setConstructor";

    private InjectionUtil() {
        //singleton constructor
    }

    public static void setField(Object target, int superLevel, String field, Object source) {
        try {
            Field classField = getSuperClass(target.getClass(), superLevel).getDeclaredField(field);
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

    public static void setMethod(Object target, int superLevel, String method, Class[] argClasses, Object[] args) {
        try {
            Method classMethod = getSuperClass(target.getClass(), superLevel).getDeclaredMethod(method, argClasses);

            boolean accessible = classMethod.isAccessible();
            classMethod.setAccessible(true);

            classMethod.invoke(target, args);

            classMethod.setAccessible(accessible);

        } catch (NoSuchMethodException e) {
            throw new RoboticsInjectionException("Exception during method injection: NoSuchFieldException", e);
        } catch (InvocationTargetException e) {
            throw new RoboticsInjectionException("Exception during method injection: InvocationTargetException", e);
        } catch (IllegalAccessException e) {
            throw new RoboticsInjectionException("Exception during method injection: IllegalAccessException", e);
        }
    }

    public static <T> T setConstructor(Class<T> targetClass, Class[] argClasses, Object[] args) {
        T output;

        try {
            Constructor classConstructor = targetClass.getDeclaredConstructor(argClasses);

            boolean accessible = classConstructor.isAccessible();
            classConstructor.setAccessible(true);

            output = (T) classConstructor.newInstance(args);

            classConstructor.setAccessible(accessible);

        } catch (InvocationTargetException e) {
            throw new RoboticsInjectionException("Exception during method injection: InvocationTargetException", e);
        } catch (NoSuchMethodException e) {
            throw new RoboticsInjectionException("Exception during method injection: NoSuchMethodException", e);
        } catch (InstantiationException e) {
            throw new RoboticsInjectionException("Exception during method injection: InstantiationException", e);
        } catch (IllegalAccessException e) {
            throw new RoboticsInjectionException("Exception during method injection: IllegalAccessException", e);
        }
        return output;
    }

    private static Class getSuperClass(Class input, int level) {
        if (level == 0) {
            return input;
        } else {
            return getSuperClass(input.getSuperclass(), level - 1);
        }
    }
}
