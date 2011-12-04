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

    private PrimitiveType primitiveType;

    public ASTPrimitiveType(PrimitiveType primitiveType) {
        this.primitiveType = primitiveType;
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
        return primitiveType.getKind().name();
    }

    @Override
    public boolean isConcreteClass() {
        return false;
    }
}
