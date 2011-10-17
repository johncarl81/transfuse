package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class ASTElementType implements ASTType {

    private TypeElement typeElement;
    private Collection<ASTMethod> methods;
    private Collection<ASTConstructor> constructors;
    private Collection<ASTField> fields;

    public ASTElementType(TypeElement typeElement, Collection<ASTConstructor> constructors, Collection<ASTMethod> methods, Collection<ASTField> fields) {
        this.typeElement = typeElement;
        this.constructors = constructors;
        this.methods = methods;
        this.fields = fields;
    }

    @Override
    public String getName() {
        return typeElement.getQualifiedName().toString();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return typeElement.getAnnotation(annotation);
    }

    @Override
    public Collection<ASTMethod> getMethods() {
        return methods;
    }

    @Override
    public Collection<ASTField> getFields() {
        return fields;
    }

    @Override
    public Collection<ASTConstructor> getConstructors() {
        return constructors;
    }
}
