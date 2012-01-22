package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class ASTClassField implements ASTField {

    private Field field;
    private ASTType astType;
    private ASTAccessModifier modifier;
    private Collection<ASTAnnotation> annotations;

    public ASTClassField(Field field, ASTType astType, ASTAccessModifier modifier, Collection<ASTAnnotation> annotations) {
        this.field = field;
        this.astType = astType;
        this.modifier = modifier;
        this.annotations = annotations;
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return field.isAnnotationPresent(annotation);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return field.getAnnotation(annotation);
    }

    @Override
    public Collection<ASTAnnotation> getAnnotations() {
        return annotations;
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }

    @Override
    public Object getConstantValue() {
        try {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            Object constantValue = field.get(null);
            field.setAccessible(accessible);
            return constantValue;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            return null;
        }
        return null;
    }
}
