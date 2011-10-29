package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author John Ericksen
 */
public class ASTClassFactory {

    private Map<Class<?>, ASTType> typeCache = new HashMap<Class<?>, ASTType>();

    public ASTType buildASTClassType(Class<?> parameterType) {
        if (!typeCache.containsKey(parameterType)) {
            Collection<ASTConstructor> constructors = new ArrayList<ASTConstructor>();
            Collection<ASTMethod> methods = new ArrayList<ASTMethod>();
            Collection<ASTField> fields = new ArrayList<ASTField>();

            typeCache.put(parameterType, new ASTClassType(parameterType, constructors, methods, fields));

            //fill in the guts after building the tree
            for (Constructor constructor : parameterType.getDeclaredConstructors()) {
                constructors.add(buildASTClassConstructor(constructor));
            }

            for (Method method : parameterType.getDeclaredMethods()) {
                methods.add(buildASTClassMethod(method));
            }

            for (Field field : parameterType.getDeclaredFields()) {
                fields.add(buildASTClassField(field));
            }
        }
        return typeCache.get(parameterType);
    }

    private List<ASTParameter> buildASTTypeParameters(Constructor constructor) {
        return buildASTTypeParameters(constructor.getParameterTypes(), constructor.getParameterAnnotations());
    }

    public List<ASTParameter> buildASTTypeParameters(Method method) {

        return buildASTTypeParameters(method.getParameterTypes(), method.getParameterAnnotations());
    }

    public List<ASTParameter> buildASTTypeParameters(Class<?>[] parameterTypes, Annotation[][] parameterAnnotations) {

        List<ASTParameter> astParameters = new ArrayList<ASTParameter>();

        for (int i = 0; i < parameterTypes.length; i++) {
            astParameters.add(buildASTClassParameter(parameterTypes[i], parameterAnnotations[i]));
        }

        return astParameters;
    }

    public ASTParameter buildASTClassParameter(Class<?> parameterType, Annotation[] annotations) {
        return new ASTClassParameter(annotations, buildASTClassType(parameterType));
    }

    public ASTMethod buildASTClassMethod(Method method) {

        List<ASTParameter> astParameters = buildASTTypeParameters(method);

        return new ASTClassMethod(method, astParameters);
    }

    public ASTField buildASTClassField(Field field) {
        return new ASTClassField(field, buildASTClassType(field.getType()));
    }

    public ASTConstructor buildASTClassConstructor(Constructor constructor) {
        return new ASTClassConstructor(constructor, buildASTTypeParameters(constructor));
    }


}
