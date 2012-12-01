package org.androidtransfuse.analysis.adapter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.model.PackageClass;

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
        typeCache.put("void", ASTVoidType.VOID);
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
        //adds a new ASTType to the cache if one does not exist yet

        ImmutableList.Builder<ASTConstructor> constructorBuilder = ImmutableList.builder();
        ImmutableList.Builder<ASTMethod> methodBuilder = ImmutableList.builder();
        ImmutableList.Builder<ASTField> fieldBuilder = ImmutableList.builder();

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
        return getParamters(method.getParameterTypes(), method.getGenericParameterTypes(), method.getParameterAnnotations());
    }

    /**
     * Builds the parameters for a set of parallel arrays: type and annotations
     *
     * @param parameterTypes
     * @param genericParameterTypes
     * @param parameterAnnotations  @return AST Parameters
     */
    public ImmutableList<ASTParameter> getParamters(Class<?>[] parameterTypes, Type[] genericParameterTypes, Annotation[][] parameterAnnotations) {

        ImmutableList.Builder<ASTParameter> astParameterBuilder = ImmutableList.builder();

        for (int i = 0; i < parameterTypes.length; i++) {
            astParameterBuilder.add(
                    new ASTClassParameter(
                            parameterAnnotations[i],
                            getType(parameterTypes[i], nullSafeAccess(genericParameterTypes, i)),
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
        ImmutableList<ASTType> throwsTypes = getTypes(method.getExceptionTypes());

        return new ASTClassMethod(method, getType(method.getReturnType(), method.getGenericReturnType()), astParameters, modifier, getAnnotations(method), throwsTypes);
    }

    private ImmutableList<ASTType> getTypes(Class<?>[] inputClasses) {
        ImmutableList.Builder<ASTType> typesBuilder = ImmutableList.builder();

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

        ImmutableList<ASTParameter> constructorParameters = getParamters(constructor.getParameterTypes(), constructor.getGenericParameterTypes(), constructor.getParameterAnnotations());
        ImmutableList<ASTType> throwsTypes = getTypes(constructor.getExceptionTypes());

        return new ASTClassConstructor(getAnnotations(constructor), constructor, constructorParameters, modifier, throwsTypes);
    }

    private ImmutableList<ASTAnnotation> getAnnotations(AnnotatedElement element) {
        return getAnnotations(element.getAnnotations());
    }

    /**
     * Build the AST Annotations from the given input annotation array.
     *
     * @param annotations
     * @return List of AST Annotations
     */
    private ImmutableList<ASTAnnotation> getAnnotations(Annotation[] annotations) {

        ImmutableList.Builder<ASTAnnotation> astAnnotationBuilder = ImmutableList.builder();

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
