package org.androidtransfuse.analysis.adapter;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * Factory building AST objects from the relevant class attributes
 *
 * @author John Ericksen
 */
public class ASTClassFactory {

    private Map<String, ASTType> typeCache = new HashMap<String, ASTType>();
    private ASTFactory astFactory;

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

            Collection<ASTConstructor> constructors = new ArrayList<ASTConstructor>();
            Collection<ASTMethod> methods = new ArrayList<ASTMethod>();
            Collection<ASTField> fields = new ArrayList<ASTField>();

            ASTType superClass = null;
            if (clazz.getSuperclass() != null) {
                superClass = buildASTClassType(clazz.getSuperclass(), clazz.getGenericSuperclass());
            }

            Collection<ASTType> interfaces = new HashSet<ASTType>();

            typeCache.put(clazz.getName(), new ASTClassType(clazz, buildAnnotations(clazz), constructors, methods, fields, superClass, interfaces));

            Class<?>[] classInterfaces = clazz.getInterfaces();
            Type[] classGenericInterfaces = clazz.getGenericInterfaces();

            for (int i = 0; i < classInterfaces.length; i++) {
                interfaces.add(buildASTClassType(classInterfaces[i], classGenericInterfaces[i]));
            }

            //fill in the guts after building the class tree
            //building after the class types have been defined avoids an infinite loop between
            //defining classes and their attributes
            for (Constructor constructor : clazz.getDeclaredConstructors()) {
                constructors.add(buildASTClassConstructor(constructor));
            }

            for (Method method : clazz.getDeclaredMethods()) {
                methods.add(buildASTClassMethod(method));
            }

            for (Field field : clazz.getDeclaredFields()) {
                fields.add(buildASTClassField(field));
            }
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
    public List<ASTParameter> buildASTTypeParameters(Method method) {

        return buildASTTypeParameters(method.getParameterTypes(), method.getGenericParameterTypes(), method.getParameterAnnotations());
    }

    /**
     * Builds the parameters for a set of parallel arrays: type and annotations
     *
     * @param parameterTypes
     * @param genericParameterTypes
     * @param parameterAnnotations  @return AST Parameters
     */
    public List<ASTParameter> buildASTTypeParameters(Class<?>[] parameterTypes, Type[] genericParameterTypes, Annotation[][] parameterAnnotations) {

        List<ASTParameter> astParameters = new ArrayList<ASTParameter>();

        for (int i = 0; i < parameterTypes.length; i++) {
            astParameters.add(
                    new ASTClassParameter(
                            parameterAnnotations[i],
                            buildASTClassType(parameterTypes[i], nullSafeAccess(genericParameterTypes, i)),
                            buildAnnotations(parameterAnnotations[i])));
        }

        return astParameters;
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

        List<ASTParameter> astParameters = buildASTTypeParameters(method);
        ASTAccessModifier modifier = ASTAccessModifier.getModifier(method.getModifiers());

        return new ASTClassMethod(method, buildASTClassType(method.getReturnType(), method.getGenericReturnType()), astParameters, modifier, buildAnnotations(method));
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

        List<ASTParameter> constructorParameters = buildASTTypeParameters(constructor.getParameterTypes(), constructor.getGenericParameterTypes(), constructor.getParameterAnnotations());

        return new ASTClassConstructor(buildAnnotations(constructor), constructor, constructorParameters, modifier);
    }

    private List<ASTAnnotation> buildAnnotations(AnnotatedElement element) {
        return buildAnnotations(element.getAnnotations());
    }

    /**
     * Build the AST Annotations from the given input annotation array.
     *
     * @param annotations
     * @return List of AST Annotations
     */
    private List<ASTAnnotation> buildAnnotations(Annotation[] annotations) {

        List<ASTAnnotation> astAnnotations = new ArrayList<ASTAnnotation>();

        for (Annotation annotation : annotations) {
            astAnnotations.add(new ASTClassAnnotation(annotation, this));
        }

        return astAnnotations;
    }

}
