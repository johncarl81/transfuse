package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTClassConstructor implements ASTConstructor {

    private Constructor constructor;
    private List<ASTParameter> parameters;
    private ASTAccessModifier modifier;

    public ASTClassConstructor(Constructor<?> constructor, List<ASTParameter> parameters, ASTAccessModifier modifier) {
        this.constructor = constructor;
        this.parameters = parameters;
        this.modifier = modifier;
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return constructor.isAnnotationPresent(annotation);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return (A) constructor.getAnnotation(annotation);
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }

    @Override
    public String getName() {
        return constructor.getName();
    }

    @Override
    public List<ASTAnnotation> getAnnotations() {
        List<ASTAnnotation> annotationList = new ArrayList<ASTAnnotation>();

        for (Annotation annotation : constructor.getAnnotations()) {
            annotationList.add(new ASTClassAnnotation(annotation));
        }

        return annotationList;
    }

    @Override
    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }
}
