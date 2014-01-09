package org.androidtransfuse.adapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author John Ericksen
 */
public class ASTWildcardType extends ASTEmptyType {

    private final ASTType superBound;
    private final ASTType extendsBound;

    public ASTWildcardType(ASTType superBound, ASTType extendsBound) {
        super("?");
        this.superBound = superBound;
        this.extendsBound = extendsBound;
    }

    public ASTType getSuperBound() {
        return superBound;
    }

    public ASTType getExtendsBound() {
        return extendsBound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTWildcardType)) {
            return false;
        }

        ASTWildcardType that = (ASTWildcardType) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(superBound, that.superBound)
                .append(extendsBound, that.extendsBound)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(superBound).append(extendsBound).hashCode();
    }
}