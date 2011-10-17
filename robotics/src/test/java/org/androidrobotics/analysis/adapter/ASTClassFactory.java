package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTClassFactory {

    private List<ASTParameter> buildASTTypeParameters(Constructor constructor) {
        return buildASTTypeParameters(constructor.getTypeParameters(), constructor.getParameterTypes(), constructor.getParameterAnnotations());
    }

    public List<ASTParameter> buildASTTypeParameters(Method method) {

        return buildASTTypeParameters(method.getTypeParameters(), method.getParameterTypes(), method.getParameterAnnotations());
    }

    public List<ASTParameter> buildASTTypeParameters(TypeVariable<Method>[] typeParameters, Class<?>[] parameterTypes, Annotation[][] parameterAnnotations) {

        List<ASTParameter> astParameters = new ArrayList<ASTParameter>();

        for (int i = 0; i < typeParameters.length; i++) {
            astParameters.add(buildASTClassParameter(typeParameters[i], parameterTypes[i], parameterAnnotations[i]));
        }

        return astParameters;
    }

    public ASTParameter buildASTClassParameter(TypeVariable typeVariable, Class<?> parameterType, Annotation[] annotations) {
        return new ASTClassParameter(typeVariable, annotations, buildASTClassType(parameterType));
    }

    public ASTType buildASTClassType(Class<?> parameterType) {
        Collection<ASTConstructor> constructors = new ArrayList<ASTConstructor>();
        Collection<ASTMethod> methods = new ArrayList<ASTMethod>();
        Collection<ASTField> fields = new ArrayList<ASTField>();

        for (Constructor constructor : parameterType.getConstructors()) {
            constructors.add(buildASTClassConstructor(constructor));
        }

        for (Method method : parameterType.getDeclaredMethods()) {
            methods.add(buildASTClassMethod(method));
        }

        for (Field field : parameterType.getDeclaredFields()) {
            fields.add(buildASTClassField(field));
        }

        return new ASTClassType(parameterType, constructors, methods, fields);
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
