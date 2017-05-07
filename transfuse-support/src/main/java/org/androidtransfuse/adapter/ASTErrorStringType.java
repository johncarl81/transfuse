package org.androidtransfuse.adapter;

import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.adapter.element.LazyASTType;
import org.androidtransfuse.transaction.TransactionRuntimeException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.inject.Provider;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author John Ericksen
 */
public class ASTErrorStringType extends LazyASTType {

    private final Elements elements;
    private final Provider<ASTElementFactory> astElementFactoryProvider;
    private final String name;

    public ASTErrorStringType(Elements elements, Provider<ASTElementFactory> astElementFactoryProvider, String name) {
        super(new PackageClass(name), elements.getTypeElement(name));
        this.name = name;
        this.astElementFactoryProvider = astElementFactoryProvider;
        this.elements = elements;
    }

    @Override
    public ASTType lazyLoad() {
        TypeElement typeElement = elements.getTypeElement(name);

        if(typeElement == null) {
            throw new TransactionRuntimeException("Encountered ErrorType " + name + ", unable to recover");
        }

        return astElementFactoryProvider.get().getType(typeElement);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean inherits(ASTType type) {
        return ASTUtils.getInstance().inherits(this, type);
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
    public String toString() {
        return getName();
    }
}
