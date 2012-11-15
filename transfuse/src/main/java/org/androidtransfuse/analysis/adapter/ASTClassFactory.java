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
    public ASTType buildASTClassType(Class<?> clazz) {
        return buildASTClassType(clazz, null);
    }

    private ASTType buildASTClassType(Class<?> clazz, Type genericType) {
        if (!typeCache.containsKey(clazz.getName())) {
            //adds a new ASTType to the cache if one does not exist yet

            ImmutableList.Builder<ASTConstructor> constructorBuilder = ImmutableList.builder();
            ImmutableList.Builder<ASTMethod> methodBuilder = ImmutableList.builder();
            ImmutableList.Builder<ASTField> fieldBuilder = ImmutableList.builder();

            ASTType superClass = null;
            if (clazz.getSuperclass() != null) {
                superClass = buildASTClassType(clazz.getSuperclass(), clazz.getGenericSuperclass());
            }

            ImmutableSet.Builder<ASTType> interfaceBuilder = ImmutableSet.builder();

            ImmutableSet.Builder<ASTAnnotation> annotationBuilder = ImmutableSet.builder();

            PackageClass packageClass = new PackageClass(clazz);

            ASTTypeVirtualProxy astClassTypeProxy = new ASTTypeVirtualProxy(packageClass);

            typeCache.put(clazz.getName(), astClassTypeProxy);

            Class<?>[] classInterfaces = clazz.getInterfaces();
            Type[] classGenericInterfaces = clazz.getGenericInterfaces();

            for (int i = 0; i < classInterfaces.length; i++) {
                interfaceBuilder.add(buildASTClassType(classInterfaces[i], classGenericInterfaces[i]));
            }

            //fill in the guts after building the class tree
            //building after the class types have been defined avoids an infinite loop between
            //defining classes and their attributes
            for (Constructor constructor : clazz.getDeclaredConstructors()) {
                constructorBuilder.add(buildASTClassConstructor(constructor));
            }

            for (Method method : clazz.getDeclaredMethods()) {
                methodBuilder.add(buildASTClassMethod(method));
            }

            for (Field field : clazz.getDeclaredFields()) {
                fieldBuilder.add(buildASTClassField(field));
            }

            annotationBuilder.addAll(buildAnnotations(clazz));

            ASTType astType = new ASTClassType(clazz, packageClass, annotationBuilder.build(),
                    constructorBuilder.build(),
                    methodBuilder.build(),
                    fieldBuilder.build(),
                    superClass,
                    interfaceBuilder.build());

            astClassTypeProxy.load(astType);
            typeCache.put(clazz.getName(), astType);
        }

        ASTType astType = typeCache.get(clazz.getName());

        if (genericType instanceof ParameterizedType) {
            //wrap with a parametrized type
            astType = astFactory.buildGenericTypeWrapper(astType, astFactory.builderParameterBuilder((ParameterizedType) genericType));
        }

        return astType;
    }

    /**
     * Builds the parameters for a given method
     *
     * @param method
     * @return AST parameters
     */
    public ImmutableList<ASTParameter> buildASTTypeParameters(Method method) {
        return buildASTTypeParameters(method.getParameterTypes(), method.getGenericParameterTypes(), method.getParameterAnnotations());
    }

    /**
     * Builds the parameters for a set of parallel arrays: type and annotations
     *
     * @param parameterTypes
     * @param genericParameterTypes
     * @param parameterAnnotations  @return AST Parameters
     */
    public ImmutableList<ASTParameter> buildASTTypeParameters(Class<?>[] parameterTypes, Type[] genericParameterTypes, Annotation[][] parameterAnnotations) {

        ImmutableList.Builder<ASTParameter> astParameterBuilder = ImmutableList.builder();

        for (int i = 0; i < parameterTypes.length; i++) {
            astParameterBuilder.add(
                    new ASTClassParameter(
                            parameterAnnotations[i],
                            buildASTClassType(parameterTypes[i], nullSafeAccess(genericParameterTypes, i)),
                            buildAnnotations(parameterAnnotations[i])));
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
    public ASTMethod buildASTClassMethod(Method method) {

        ImmutableList<ASTParameter> astParameters = buildASTTypeParameters(method);
        ASTAccessModifier modifier = ASTAccessModifier.getModifier(method.getModifiers());
        ImmutableList<ASTType> throwsTypes = buildASTClassTypes(method.getExceptionTypes());

        return new ASTClassMethod(method, buildASTClassType(method.getReturnType(), method.getGenericReturnType()), astParameters, modifier, buildAnnotations(method), throwsTypes);
    }

    private ImmutableList<ASTType> buildASTClassTypes(Class<?>[] inputClasses) {
        ImmutableList.Builder<ASTType> typesBuilder = ImmutableList.builder();

        for (Class<?> inputClass : inputClasses) {
            typesBuilder.add(buildASTClassType(inputClass));
        }

        return typesBuilder.build();
    }

    /**
     * Builds an AST Field from the given field
     *
     * @param field
     * @return AST Field
     */
    public ASTField buildASTClassField(Field field) {
        ASTAccessModifier modifier = ASTAccessModifier.getModifier(field.getModifiers());

        return new ASTClassField(field, buildASTClassType(field.getType(), field.getGenericType()), modifier, buildAnnotations(field));
    }

    /**
     * Build an AST Constructor from the given constructor
     *
     * @param constructor
     * @return AST Constructor
     */
    public ASTConstructor buildASTClassConstructor(Constructor constructor) {
        ASTAccessModifier modifier = ASTAccessModifier.getModifier(constructor.getModifiers());

        ImmutableList<ASTParameter> constructorParameters = buildASTTypeParameters(constructor.getParameterTypes(), constructor.getGenericParameterTypes(), constructor.getParameterAnnotations());
        ImmutableList<ASTType> throwsTypes = buildASTClassTypes(constructor.getExceptionTypes());

        return new ASTClassConstructor(buildAnnotations(constructor), constructor, constructorParameters, modifier, throwsTypes);
    }

    private ImmutableList<ASTAnnotation> buildAnnotations(AnnotatedElement element) {
        return buildAnnotations(element.getAnnotations());
    }

    /**
     * Build the AST Annotations from the given input annotation array.
     *
     * @param annotations
     * @return List of AST Annotations
     */
    private ImmutableList<ASTAnnotation> buildAnnotations(Annotation[] annotations) {

        ImmutableList.Builder<ASTAnnotation> astAnnotationBuilder = ImmutableList.builder();

        for (Annotation annotation : annotations) {
            astAnnotationBuilder.add(buildAnnotation(annotation));
        }

        return astAnnotationBuilder.build();
    }

    public ASTAnnotation buildAnnotation(Annotation annotation) {
        ASTType type = buildASTClassType(annotation.annotationType());
        return new ASTClassAnnotation(annotation, type, this);
    }

}
