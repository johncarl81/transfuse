package org.androidtransfuse.analysis.adapter;

import com.google.common.collect.ImmutableCollection;
import org.androidtransfuse.model.PackageClass;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Element specific AST Type
 *
 * @author John Ericksen
 */
public class ASTElementType extends ASTElementBase implements ASTType {

    private final TypeElement typeElement;
    private final PackageClass packageClass;
    private final ImmutableCollection<ASTMethod> methods;
    private final ImmutableCollection<ASTConstructor> constructors;
    private final ImmutableCollection<ASTField> fields;
    private final ImmutableCollection<ASTType> interfaces;
    private final ASTType superClass;

    public ASTElementType(PackageClass packageClass,
                          TypeElement typeElement,
                          ImmutableCollection<ASTConstructor> constructors,
                          ImmutableCollection<ASTMethod> methods,
                          ImmutableCollection<ASTField> fields,
                          ASTType superClass,
                          ImmutableCollection<ASTType> interfaces,
                          ImmutableCollection<ASTAnnotation> annotations) {
        super(typeElement, annotations);
        this.packageClass = packageClass;
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
    public PackageClass getPackageClass() {
        return packageClass;
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
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTType)) {
            return false;
        }

        ASTType that = (ASTType) o;

        return new EqualsBuilder().append(getName(), that.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getName()).hashCode();
    }

    @Override
    public List<ASTType> getGenericParameters() {
        return Collections.emptyList();
    }

    @Override
    public boolean inheritsFrom(ASTType type) {
        return ASTUtils.getInstance().inherits(this, type, true, true);
    }

    @Override
    public boolean extendsFrom(ASTType type) {
        return ASTUtils.getInstance().inherits(this, type, false, true);
    }

    @Override
    public boolean implementsFrom(ASTType type) {
        return ASTUtils.getInstance().inherits(this, type, true, false);
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }

    @Override
    public String toString() {
        return getName();
    }
}
