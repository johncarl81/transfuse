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
package org.androidtransfuse.adapter.classes;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory building AST objects from the relevant class attributes
 *
 * @author John Ericksen
 */
@Singleton
public class ASTClassFactory {

    private final Map<String, ASTType> typeCache = new HashMap<String, ASTType>();
    private final ASTFactory astFactory;

    @Inject
    public ASTClassFactory(ASTFactory astFactory) {
        this.astFactory = astFactory;
        //seed with primitives and void
        typeCache.put(ASTVoidType.VOID.getName(), ASTVoidType.VOID);
        for (ASTPrimitiveType primitive : ASTPrimitiveType.values()) {
            typeCache.put(primitive.getName(), primitive);
        }
    }

    /**
     * Build an ASTType from the given class
     *
     * @param clazz input Class
     * @return ASTType representing input class
     */
    public ASTType getType(Class<?> clazz) {
        return getType(clazz, null);
    }

    private synchronized ASTType getType(Class<?> clazz, Type genericType) {
        if (!typeCache.containsKey(clazz.getName())) {
            typeCache.put(clazz.getName(), buildType(clazz));
        }

        ASTType astType = typeCache.get(clazz.getName());

        if (genericType instanceof ParameterizedType) {
            //wrap with a parametrized type
            astType = astFactory.buildGenericTypeWrapper(astType, astFactory.builderParameterBuilder((ParameterizedType) genericType));
        }

        return astType;
    }

    private ASTType buildType(Class<?> clazz) {
        ImmutableSet.Builder<ASTConstructor> constructorBuilder = ImmutableSet.builder();
        ImmutableSet.Builder<ASTMethod> methodBuilder = ImmutableSet.builder();
        ImmutableSet.Builder<ASTField> fieldBuilder = ImmutableSet.builder();

        ASTType superClass = null;
        if (clazz.getSuperclass() != null) {
            superClass = getType(clazz.getSuperclass(), clazz.getGenericSuperclass());
        }

        ImmutableSet.Builder<ASTType> interfaceBuilder = ImmutableSet.builder();

        ImmutableSet.Builder<ASTAnnotation> annotationBuilder = ImmutableSet.builder();

        PackageClass packageClass = new PackageClass(clazz);

        ASTTypeVirtualProxy astClassTypeProxy = new ASTTypeVirtualProxy(packageClass);

        typeCache.put(clazz.getName(), astClassTypeProxy);

        Class<?>[] classInterfaces = clazz.getInterfaces();
        Type[] classGenericInterfaces = clazz.getGenericInterfaces();

        for (int i = 0; i < classInterfaces.length; i++) {
            interfaceBuilder.add(getType(classInterfaces[i], classGenericInterfaces[i]));
        }

        //fill in the guts after building the class tree
        //building after the class types have been defined avoids an infinite loop between
        //defining classes and their attributes
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            constructorBuilder.add(getConstructor(constructor));
        }

        for (Method method : clazz.getDeclaredMethods()) {
            methodBuilder.add(getMethod(method));
        }

        for (Field field : clazz.getDeclaredFields()) {
            fieldBuilder.add(getField(field));
        }

        annotationBuilder.addAll(getAnnotations(clazz));

        ASTType astType = new ASTClassType(clazz, packageClass, annotationBuilder.build(),
                constructorBuilder.build(),
                methodBuilder.build(),
                fieldBuilder.build(),
                superClass,
                interfaceBuilder.build());

        astClassTypeProxy.load(astType);

        return astType;
    }


    /**
     * Builds the parameters for a given method
     *
     * @param method
     * @return AST parameters
     */
    public ImmutableList<ASTParameter> getParameters(Method method) {
        return getParameters(method.getParameterTypes(), method.getGenericParameterTypes(), method.getParameterAnnotations());
    }

    /**
     * Builds the parameters for a set of parallel arrays: type and annotations
     *
     * @param parameterTypes
     * @param genericParameterTypes
     * @param parameterAnnotations  @return AST Parameters
     */
    public ImmutableList<ASTParameter> getParameters(Class<?>[] parameterTypes, Type[] genericParameterTypes, Annotation[][] parameterAnnotations) {

        ImmutableList.Builder<ASTParameter> astParameterBuilder = ImmutableList.builder();

        for (int i = 0; i < parameterTypes.length; i++) {
            ASTType parameterType = getType(parameterTypes[i], nullSafeAccess(genericParameterTypes, i));
            astParameterBuilder.add(
                    new ASTClassParameter(
                            parameterType.getName(),
                            parameterAnnotations[i],
                            parameterType,
                            getAnnotations(parameterAnnotations[i])));
        }

        return astParameterBuilder.build();
    }

    private Type nullSafeAccess(Type[] typeArray, int i) {
        if (typeArray.length > i) {
            return typeArray[i];
        }
        return null;
    }

    /**
     * Builds an AST Method fromm the given input method.
     *
     * @param method
     * @return AST Method
     */
    public ASTMethod getMethod(Method method) {

        ImmutableList<ASTParameter> astParameters = getParameters(method);
        ASTAccessModifier modifier = ASTAccessModifier.getModifier(method.getModifiers());
        ImmutableSet<ASTType> throwsTypes = getTypes(method.getExceptionTypes());

        return new ASTClassMethod(method, getType(method.getReturnType(), method.getGenericReturnType()), astParameters, modifier, getAnnotations(method), throwsTypes);
    }

    private ImmutableSet<ASTType> getTypes(Class<?>[] inputClasses) {
        ImmutableSet.Builder<ASTType> typesBuilder = ImmutableSet.builder();

        for (Class<?> inputClass : inputClasses) {
            typesBuilder.add(getType(inputClass));
        }

        return typesBuilder.build();
    }

    /**
     * Builds an AST Field from the given field
     *
     * @param field
     * @return AST Field
     */
    public ASTField getField(Field field) {
        ASTAccessModifier modifier = ASTAccessModifier.getModifier(field.getModifiers());

        return new ASTClassField(field, getType(field.getType(), field.getGenericType()), modifier, getAnnotations(field));
    }

    /**
     * Build an AST Constructor from the given constructor
     *
     * @param constructor
     * @return AST Constructor
     */
    public ASTConstructor getConstructor(Constructor constructor) {
        ASTAccessModifier modifier = ASTAccessModifier.getModifier(constructor.getModifiers());

        ImmutableList<ASTParameter> constructorParameters = getParameters(constructor.getParameterTypes(), constructor.getGenericParameterTypes(), constructor.getParameterAnnotations());
        ImmutableSet<ASTType> throwsTypes = getTypes(constructor.getExceptionTypes());

        return new ASTClassConstructor(getAnnotations(constructor), constructor, constructorParameters, modifier, throwsTypes);
    }

    private ImmutableSet<ASTAnnotation> getAnnotations(AnnotatedElement element) {
        return getAnnotations(element.getAnnotations());
    }

    /**
     * Build the AST Annotations from the given input annotation array.
     *
     * @param annotations
     * @return List of AST Annotations
     */
    private ImmutableSet<ASTAnnotation> getAnnotations(Annotation[] annotations) {

        ImmutableSet.Builder<ASTAnnotation> astAnnotationBuilder = ImmutableSet.builder();

        for (Annotation annotation : annotations) {
            astAnnotationBuilder.add(getAnnotation(annotation));
        }

        return astAnnotationBuilder.build();
    }

    public ASTAnnotation getAnnotation(Annotation annotation) {
        ASTType type = getType(annotation.annotationType());
        return new ASTClassAnnotation(annotation, type, this);
    }

}
