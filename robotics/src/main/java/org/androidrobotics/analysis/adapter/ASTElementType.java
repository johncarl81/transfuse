package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.TypeElement;
import java.util.Collection;

/**
 * Element specific Type of an AST object
 *
 * @author John Ericksen
 */
public class ASTElementType extends ASTElementBase implements ASTType {

    private TypeElement typeElement;
    private Collection<ASTMethod> methods;
    private Collection<ASTConstructor> constructors;
    private Collection<ASTField> fields;
    private Collection<ASTType> interfaces;
    private ASTType superClass;

    public ASTElementType(TypeElement typeElement, Collection<ASTConstructor> constructors, Collection<ASTMethod> methods, Collection<ASTField> fields, ASTType superClass, Collection<ASTType> interfaces) {
        super(typeElement);
        this.typeElement = typeElement;
        this.constructors = constructors;
        this.methods = methods;
        this.fields = fields;
        this.superClass = superClass;
        this.interfaces = interfaces;
    }

    @Override
    public String getName() {
        return typeElement.getQualifiedName().toString();
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

    @Override
    public boolean isConcreteClass() {
        return typeElement.getKind().isClass();
    }

    @Override
    public ASTType getSuperClass() {
        return superClass;
    }

    @Override
    public Collection<ASTType> getInterfaces() {
        return interfaces;
    }

    @Override
    public boolean isArray() {
        return false;//todo: support array types
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ASTElementType)) return false;

        ASTElementType that = (ASTElementType) o;

        if (typeElement != null ? !typeElement.equals(that.typeElement) : that.typeElement != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return typeElement != null ? typeElement.hashCode() : 0;
    }
}
