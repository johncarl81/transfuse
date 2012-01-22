package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author John Ericksen
 */
public class ASTClassFactory {

    private Map<String, ASTType> typeCache = new HashMap<String, ASTType>();

    public ASTClassFactory() {
        //seed with primitives and void
        typeCache.put("void", ASTVoidType.VOID);
        for (ASTPrimitiveType primitive : ASTPrimitiveType.values()) {
            typeCache.put(primitive.getName(), primitive);
        }
    }

    public ASTType buildASTClassType(Class<?> clazz) {
        if (!typeCache.containsKey(clazz.getName())) {
            Collection<ASTConstructor> constructors = new ArrayList<ASTConstructor>();
            Collection<ASTMethod> methods = new ArrayList<ASTMethod>();
            Collection<ASTField> fields = new ArrayList<ASTField>();
            ASTType superClass = null;
            if (clazz.getSuperclass() != null) {
                superClass = buildASTClassType(clazz.getSuperclass());
            }
            Collection<ASTType> interfaces = new HashSet<ASTType>();

            for (Class<?> clazzInterface : clazz.getInterfaces()) {
                interfaces.add(buildASTClassType(clazzInterface));
            }

            typeCache.put(clazz.getName(), new ASTClassType(clazz, buildAnnotations(clazz), constructors, methods, fields, superClass, interfaces));

            //fill in the guts after building the tree
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
        return typeCache.get(clazz.getName());
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
        return new ASTClassParameter(annotations, buildASTClassType(parameterType), buildAnnotations(annotations));
    }

    public ASTMethod buildASTClassMethod(Method method) {

        List<ASTParameter> astParameters = buildASTTypeParameters(method);
        ASTAccessModifier modifier = buildASTAccessModifier(method.getModifiers());

        return new ASTClassMethod(method, buildASTClassType(method.getReturnType()), astParameters, modifier, buildAnnotations(method));
    }

    private ASTAccessModifier buildASTAccessModifier(int modifiers) {
        switch (modifiers) {
            case Modifier.PUBLIC:
                return ASTAccessModifier.PUBLIC;
            case Modifier.PROTECTED:
                return ASTAccessModifier.PROTECTED;
            case Modifier.PRIVATE:
                return ASTAccessModifier.PRIVATE;
        }

        return ASTAccessModifier.PACKAGE_PRIVATE;
    }

    public ASTField buildASTClassField(Field field) {
        ASTAccessModifier modifier = buildASTAccessModifier(field.getModifiers());

        return new ASTClassField(field, buildASTClassType(field.getType()), modifier, buildAnnotations(field));
    }

    public ASTConstructor buildASTClassConstructor(Constructor constructor) {
        ASTAccessModifier modifier = buildASTAccessModifier(constructor.getModifiers());

        return new ASTClassConstructor(buildAnnotations(constructor), constructor, buildASTTypeParameters(constructor), modifier);
    }

    private List<ASTAnnotation> buildAnnotations(AnnotatedElement element) {
        return buildAnnotations(element.getAnnotations());
    }

    private List<ASTAnnotation> buildAnnotations(Annotation[] annotations) {

        List<ASTAnnotation> astAnnotations = new ArrayList<ASTAnnotation>();

        for (Annotation annotation : annotations) {
            astAnnotations.add(new ASTClassAnnotation(annotation));
        }

        return astAnnotations;
    }

}
