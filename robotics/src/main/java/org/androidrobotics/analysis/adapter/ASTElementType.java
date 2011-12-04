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

    public ASTElementType(TypeElement typeElement, Collection<ASTConstructor> constructors, Collection<ASTMethod> methods, Collection<ASTField> fields) {
        super(typeElement);
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
}
