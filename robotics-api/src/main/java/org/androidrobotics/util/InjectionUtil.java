package org.androidrobotics.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;

/**
 * @author John Ericksen
 */
public final class InjectionUtil {

    public static final String SET_FIELD_METHOD = "setField";
    public static final String SET_METHOD_METHOD = "setMethod";
    public static final String SET_CONSTRUCTOR_METHOD = "setConstructor";

    private InjectionUtil() {
        //singleton constructor
    }

    public static void setField(Object target, int superLevel, String field, Object source) {
        try {
            Field classField = getSuperClass(target.getClass(), superLevel).getDeclaredField(field);

            AccessController.doPrivileged(
                    new SetFieldPrivilegedAction(classField, target, source));

        } catch (NoSuchFieldException e) {
            throw new RoboticsInjectionException("NoSuchFieldException Exception during field injection", e);
        } catch (PrivilegedActionException e) {
            throw new RoboticsInjectionException("PrivilegedActionException Exception during field injection", e);
        } catch (Exception e) {
            throw new RoboticsInjectionException("Exception during field injection", e);
        }
    }

    private static final class SetFieldPrivilegedAction extends AccessibleElementPrivilegedAction<Void, Field> {

        private Object target;
        private Object source;

        private SetFieldPrivilegedAction(Field classField, Object target, Object source) {
            super(classField);
            this.target = target;
            this.source = source;
        }

        @Override
        public Void run(Field classField) throws java.lang.Exception {
            classField.set(target, source);

            return null;
        }
    }

    public static void setMethod(Object target, int superLevel, String method, Class[] argClasses, Object[] args) {
        try {
            Method classMethod = getSuperClass(target.getClass(), superLevel).getDeclaredMethod(method, argClasses);

            AccessController.doPrivileged(
                    new SetMethodPrivilegedAction(classMethod, target, args));

        } catch (NoSuchMethodException e) {
            throw new RoboticsInjectionException("Exception during method injection: NoSuchFieldException", e);
        } catch (PrivilegedActionException e) {
            throw new RoboticsInjectionException("PrivilegedActionException Exception during field injection", e);
        } catch (Exception e) {
            throw new RoboticsInjectionException("Exception during field injection", e);
        }
    }

    private static final class SetMethodPrivilegedAction extends AccessibleElementPrivilegedAction<Void, Method> {

        private Object target;
        private Object[] args;

        private SetMethodPrivilegedAction(Method classMethod, Object target, Object[] args) {
            super(classMethod);
            this.target = target;
            this.args = args;
        }

        public Void run(Method classMethod) throws Exception {
            classMethod.invoke(target, args);
            return null;
        }
    }


    public static <T> T setConstructor(Class<T> targetClass, Class[] argClasses, Object[] args) {
        T output;

        try {
            Constructor classConstructor = targetClass.getDeclaredConstructor(argClasses);

            output = AccessController.doPrivileged(
                    new SetConstructorPrivilegedAction<T>(classConstructor, args));

        } catch (NoSuchMethodException e) {
            throw new RoboticsInjectionException("Exception during method injection: NoSuchMethodException", e);
        } catch (PrivilegedActionException e) {
            throw new RoboticsInjectionException("PrivilegedActionException Exception during field injection", e);
        } catch (Exception e) {
            throw new RoboticsInjectionException("Exception during field injection", e);
        }
        return output;
    }

    private static final class SetConstructorPrivilegedAction<T> extends AccessibleElementPrivilegedAction<T, Constructor> {
        private Object[] args;

        private SetConstructorPrivilegedAction(Constructor classConstructor, Object[] args) {
            super(classConstructor);
            this.args = args;
        }

        @Override
        public T run(Constructor classConstructor) throws Exception {
            return (T) classConstructor.newInstance(args);
        }
    }

    private static Class getSuperClass(Class input, int level) {
        if (level == 0) {
            return input;
        } else {
            return getSuperClass(input.getSuperclass(), level - 1);
        }
    }
}
