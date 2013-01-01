/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;

/**
 * @author John Ericksen
 */
public final class InjectionUtil {

    private static final InjectionUtil INSTANCE = new InjectionUtil();

    public static final String GET_INSTANCE_METHOD = "getInstance";
    public static final String GET_FIELD_METHOD = "getField";
    public static final String SET_FIELD_METHOD = "setField";
    public static final String CALL_METHOD_METHOD = "callMethod";
    public static final String CALL_CONSTRUCTOR_METHOD = "callConstructor";

    public static InjectionUtil getInstance() {
        return INSTANCE;
    }

    private InjectionUtil() {
        //singleton constructor
    }

    public <T> T getField(Class<T> returnType, Class<?> targetClass, Object target, String field) {
        try {
            Field declaredField = targetClass.getDeclaredField(field);

            return AccessController.doPrivileged(
                    new GetFieldPrivilegedAction<T>(declaredField, target));

        } catch (NoSuchFieldException e) {
            throw new TransfuseInjectionException(
                    "NoSuchFieldException Exception during field injection: " + field + " in " + target.getClass(), e);
        } catch (PrivilegedActionException e) {
            throw new TransfuseInjectionException("PrivilegedActionException Exception during field injection", e);
        } catch (Exception e) {
            throw new TransfuseInjectionException("Exception during field injection", e);
        }
    }

    private static final class GetFieldPrivilegedAction<T> extends AccessibleElementPrivilegedAction<T, Field> {

        private final Object target;

        private GetFieldPrivilegedAction(Field classField, Object target) {
            super(classField);
            this.target = target;
        }

        @Override
        public T run(Field classField) throws IllegalAccessException {
            return (T) classField.get(target);
        }
    }

    public void setField(Class<?> targetClass, Object target, String field, Object source) {
        try {
            Field classField = targetClass.getDeclaredField(field);

            AccessController.doPrivileged(
                    new SetFieldPrivilegedAction(classField, target, source));

        } catch (NoSuchFieldException e) {
            throw new TransfuseInjectionException(
                    "NoSuchFieldException Exception during field injection: " + field + " in " + target.getClass(), e);
        } catch (PrivilegedActionException e) {
            throw new TransfuseInjectionException("PrivilegedActionException Exception during field injection", e);
        } catch (Exception e) {
            throw new TransfuseInjectionException("Exception during field injection", e);
        }
    }

    private static final class SetFieldPrivilegedAction extends AccessibleElementPrivilegedAction<Void, Field> {

        private final Object target;
        private final Object source;

        private SetFieldPrivilegedAction(Field classField, Object target, Object source) {
            super(classField);
            this.target = target;
            this.source = source;
        }

        @Override
        public Void run(Field classField) throws IllegalAccessException {
            classField.set(target, source);

            return null;
        }
    }

    public <T> T callMethod(Class<T> retClass, Class<?> targetClass, Object target, String method, Class[] argClasses, Object[] args) {
        try {
            Method classMethod = targetClass.getDeclaredMethod(method, argClasses);

            return AccessController.doPrivileged(
                    new SetMethodPrivilegedAction<T>(classMethod, target, args));

        } catch (NoSuchMethodException e) {
            throw new TransfuseInjectionException("Exception during method injection: NoSuchFieldException", e);
        } catch (PrivilegedActionException e) {
            throw new TransfuseInjectionException("PrivilegedActionException Exception during field injection", e);
        } catch (Exception e) {
            throw new TransfuseInjectionException("Exception during field injection", e);
        }
    }

    private static final class SetMethodPrivilegedAction<T> extends AccessibleElementPrivilegedAction<T, Method> {

        private final Object target;
        private final Object[] args;

        private SetMethodPrivilegedAction(Method classMethod, Object target, Object[] args) {
            super(classMethod);
            this.target = target;
            this.args = args;
        }

        public T run(Method classMethod) throws InvocationTargetException, IllegalAccessException {
            return (T) classMethod.invoke(target, args);
        }
    }


    public <T> T callConstructor(Class<T> targetClass, Class[] argClasses, Object[] args) {
        T output;

        try {
            Constructor classConstructor = targetClass.getDeclaredConstructor(argClasses);

            output = AccessController.doPrivileged(
                    new SetConstructorPrivilegedAction<T>(classConstructor, args));

        } catch (NoSuchMethodException e) {
            throw new TransfuseInjectionException("Exception during method injection: NoSuchMethodException", e);
        } catch (PrivilegedActionException e) {
            throw new TransfuseInjectionException("PrivilegedActionException Exception during field injection", e);
        } catch (Exception e) {
            throw new TransfuseInjectionException("Exception during field injection", e);
        }
        return output;
    }

    private static final class SetConstructorPrivilegedAction<T> extends AccessibleElementPrivilegedAction<T, Constructor> {
        private final Object[] args;

        private SetConstructorPrivilegedAction(Constructor classConstructor, Object[] args) {
            super(classConstructor);
            this.args = args;
        }

        @Override
        public T run(Constructor classConstructor) throws InvocationTargetException, InstantiationException, IllegalAccessException {
            return (T) classConstructor.newInstance(args);
        }
    }
}
