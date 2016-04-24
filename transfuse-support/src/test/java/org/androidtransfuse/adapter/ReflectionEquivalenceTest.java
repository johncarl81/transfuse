/**
 * Copyright 2011-2015 John Ericksen
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
package org.androidtransfuse.adapter;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
public class ReflectionEquivalenceTest {

    private static final ASTType OBJECT_TYPE = new ASTStringType(Object.class.getCanonicalName());

    private ASTClassFactory classFactory;
    private Set<Class> visited;

    @Before
    public void setup() {
        classFactory = new ASTClassFactory();
        visited = new HashSet<Class>();
    }

    private static class Base {
        String value4;

        public String getValue4() {
            return value4;
        }

        public void setValue4(String value4) {
            this.value4 = value4;
        }
    }

    public static class TestTarget extends Base implements Serializable {
        private static final long serialVersionUID = 1198606035702824074L;
        String[] value1;
        String value2;
        int value3;

        public TestTarget(String[] value1, String value2, int value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        public TestTarget(String value2, int value3) {
            this(null, value2, value3);
        }

        public static long getSerialVersionUID() {
            return serialVersionUID;
        }

        public String[] getValue1() {
            return value1;
        }

        public void setValue1(String[] value1) {
            this.value1 = value1;
        }

        public String getValue2() {
            return value2;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }

        public int getValue3() {
            return value3;
        }

        public void setValue3(int value3) {
            this.value3 = value3;
        }
    }

    @Test
    public void testReflectionEquivalence() {
        ASTType testTargetType = classFactory.getType(TestTarget.class);
        Class<TestTarget> testTargetClass = TestTarget.class;

        equals(testTargetClass, testTargetType);
    }

    private void equals(Class clazz, ASTType type) {
        if(visited.contains(clazz)){
            return;
        }
        visited.add(clazz);

        assertEquals(clazz.getName(), !clazz.isInterface(), type.isConcreteClass());
        assertEquals(clazz.isInterface(), type.isInterface());
        assertEquals(java.lang.reflect.Modifier.isFinal(clazz.getModifiers()), type.isFinal());
        assertEquals(java.lang.reflect.Modifier.isStatic(clazz.getModifiers()), type.isStatic());
        assertEquals(clazz.isEnum(), type.isEnum());
        assertEquals(Modifier.isStatic(clazz.getModifiers()), type.isStatic());
        assertEquals(Modifier.isFinal(clazz.getModifiers()), type.isFinal());
        assertEquals(clazz.getCanonicalName(), type.getName());
        assertEquals(ASTAccessModifier.getModifier(clazz.getModifiers()), type.getAccessModifier());

        assertTrue((clazz.getSuperclass() == null || clazz.getSuperclass().equals(Object.class)) ==
                ((type.getSuperClass() == null || type.getSuperClass().equals(OBJECT_TYPE))));
        if(clazz.getSuperclass() != null) {
            equals(clazz.getSuperclass(), type.getSuperClass());
        }

        if(!clazz.isPrimitive() && !clazz.isArray()) {
            assertEquals(clazz.getInterfaces().length, type.getInterfaces().size());

            for (Class clazzInterface : clazz.getInterfaces()) {
                ASTType typeInterface = findInterface(type, clazzInterface);

                if(typeInterface == null){
                    assertTrue("Unable to find interface " + clazzInterface.getCanonicalName(), false);
                }

                equals(clazzInterface, typeInterface);
            }

            assertEquals(filter(clazz.getDeclaredMethods()).size(), type.getMethods().size());
            for (Method method : filter(clazz.getDeclaredMethods())) {
                ASTMethod typeMethod = findMethod(type, method);

                if(typeMethod == null){
                    assertTrue("Unable to find method " + method, false);
                }

                assertEquals(ASTAccessModifier.getModifier(method.getModifiers()), typeMethod.getAccessModifier());
                assertEquals(Modifier.isStatic(method.getModifiers()), typeMethod.isStatic());
                assertEquals(Modifier.isFinal(method.getModifiers()), typeMethod.isFinal());
                equals(method.getReturnType(), typeMethod.getReturnType());

                for(int i = 0; i < method.getParameters().length; i++) {
                    equals(method.getParameters()[i].getType(), typeMethod.getParameters().get(i).getASTType());
                }
            }

            assertEquals(filter(clazz.getDeclaredFields()).size(), type.getFields().size());
            for (Field field : filter(clazz.getDeclaredFields())) {
                ASTField typeField = findField(type, field);

                if(typeField == null){
                    assertTrue("Unable to find field " + field, false);
                }

                assertEquals(ASTAccessModifier.getModifier(field.getModifiers()), typeField.getAccessModifier());
                assertEquals(Modifier.isTransient(field.getModifiers()), typeField.isTransient());
                assertEquals(Modifier.isStatic(field.getModifiers()), typeField.isStatic());
                assertEquals(Modifier.isFinal(field.getModifiers()), typeField.isFinal());
                equals(field.getType(), typeField.getASTType());
            }

            assertEquals(filter(clazz.getDeclaredConstructors()).size(), type.getConstructors().size());
            for (Constructor constructor : filter(clazz.getDeclaredConstructors())) {
                int parameterOffset = 0;
                if(clazz.isEnum()) {
                    parameterOffset = 2;
                }
                if(clazz.getDeclaringClass() != null && !Modifier.isStatic(clazz.getModifiers())){
                    parameterOffset = 1;
                }
                ASTConstructor typeConstructor = findConstructor(type, constructor, parameterOffset);

                if(typeConstructor == null){
                    assertTrue("Unable to find constructor " + constructor, false);
                }

                for(int i = parameterOffset; i < constructor.getParameters().length; i++) {
                    equals(constructor.getParameters()[i].getType(), typeConstructor.getParameters().get(i - parameterOffset).getASTType());
                }
            }

            assertEquals(clazz.getTypeParameters().length, type.getGenericParameters().size());
            for(int i = 0; i < clazz.getTypeParameters().length; i++) {
                if(clazz.getTypeParameters()[i] instanceof ParameterizedType) {
                    Class genericType = (Class)((ParameterizedType)clazz.getTypeParameters()[i]).getRawType();
                    equals(genericType, type.getGenericParameters().get(i));
                }
            }
        }



        // ImmutableList<ASTType> getGenericParameters();

    }

    private ASTConstructor findConstructor(ASTType type, Constructor constructor, int sizeOffset) {

        for (ASTConstructor typeConstructor : type.getConstructors()) {
            if(typeConstructor.getParameters().size() == constructor.getParameters().length - sizeOffset){
                boolean found = true;

                for(int i = sizeOffset; i < constructor.getParameters().length; i++) {
                    if(!classFactory.getType(constructor.getParameters()[i].getType()).equals(typeConstructor.getParameters().get(i - sizeOffset).getASTType())){
                        found = false;
                    }
                }

                if(found) {
                    return typeConstructor;
                }
            }
        }
        return null;
    }

    private List<Method> filter(Method[] declaredMethods) {
        return FluentIterable.from(Arrays.asList(declaredMethods))
                .filter(new Predicate<Method>() {
                    @Override
                    public boolean apply(Method method) {
                        return !method.isBridge() && !method.isSynthetic();
                    }
                })
                .toList();
    }

    private List<Field> filter(Field[] fields) {
        return FluentIterable.from(Arrays.asList(fields))
                .filter(new Predicate<Field>() {
                    @Override
                    public boolean apply(Field field) {
                        return !field.isSynthetic();
                    }
                })
                .toList();
    }

    private List<Constructor> filter(Constructor[] constructors) {
        return FluentIterable.from(Arrays.asList(constructors))
                .filter(new Predicate<Constructor>() {
                    @Override
                    public boolean apply(Constructor constructor) {
                        return !constructor.isSynthetic();
                    }
                })
                .toList();
    }

    private ASTField findField(ASTType type, Field field) {
        for (ASTField astField : type.getFields()) {
            if(astField.getName().equals(field.getName())){
                return astField;
            }
        }
        return null;
    }

    private ASTMethod findMethod(ASTType type, Method method) {
        MethodSignature clazzMethodSignature = buildMethodSignature(method);
        for (ASTMethod astMethod : type.getMethods()) {
            MethodSignature astMethodSignature = new MethodSignature(astMethod);
            if(astMethodSignature.equals(clazzMethodSignature)) {
                return astMethod;
            }
        }
        return null;
    }

    private MethodSignature buildMethodSignature(Method method) {
        List<ASTType> paramTypes = FluentIterable.from(Arrays.asList(method.getParameterTypes()))
                .transform(new Function<Class<?>, ASTType>() {
                    @Override
                    public ASTType apply(@Nullable Class<?> clazz) {
                        return classFactory.getType(clazz);
                    }
                })
                .toList();
        return new MethodSignature(method.getName(), paramTypes);
    }

    private ASTType findInterface(ASTType type, Class clazzInterface) {
        for (ASTType typeInterface : type.getInterfaces()) {
            if(typeInterface.getName().equals(clazzInterface.getCanonicalName())){
                return typeInterface;
            }
        }
        return null;
    }
}
