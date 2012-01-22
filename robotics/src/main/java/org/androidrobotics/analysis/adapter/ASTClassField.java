package org.androidrobotics.analysis.adapter;

import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.util.AccessibleElementPrivilegedAction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
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
            return AccessController.doPrivileged(
                    new PrivateConstantFieldAccessPrivilegedAction(field));
        } catch (NullPointerException e) {
            return null;
        } catch (PrivilegedActionException e) {
            throw new RoboticsAnalysisException("PrivilegedActionException when trying to set field: " + field, e);
        }
    }

    private static class PrivateConstantFieldAccessPrivilegedAction extends AccessibleElementPrivilegedAction<Object, Field> {

        protected PrivateConstantFieldAccessPrivilegedAction(Field accessibleObject) {
            super(accessibleObject);
        }

        @Override
        public Object run(Field classField) throws Exception {
            return classField.get(null);
        }


    }
}
