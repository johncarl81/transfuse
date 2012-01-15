package org.androidrobotics.analysis.adapter;

import javax.lang.model.type.PrimitiveType;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

/**
 * Element specific primitive AST type
 *
 * @author John Ericksen
 */
public class ASTPrimitiveType implements ASTType {

    private String name;

    public ASTPrimitiveType(PrimitiveType primitiveType) {
        this.name = primitiveType.getKind().name().toLowerCase();
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return false;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return null;
    }

    @Override
    public Collection<ASTMethod> getMethods() {
        return Collections.emptySet();
    }

    @Override
    public Collection<ASTField> getFields() {
        return Collections.emptySet();
    }

    @Override
    public Collection<ASTConstructor> getConstructors() {
        return Collections.emptySet();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isConcreteClass() {
        return false;
    }

    @Override
    public Collection<ASTAnnotation> getAnnotations() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ASTType> getInterfaces() {
        return Collections.emptySet();
    }

    @Override
    public ASTType getSuperClass() {
        return null;
    }
}
