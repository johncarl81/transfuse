package org.androidtransfuse.adapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author John Ericksen
 */
public class ASTGenericParameterType extends ASTProxyType {

    private final ASTGenericArgument argument;

    public ASTGenericParameterType(ASTGenericArgument argument, ASTType delegate) {
        super(delegate, delegate.getName());

        this.argument = argument;
    }

    public ASTGenericArgument getArgument() {
        return argument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ASTGenericParameterType)) return false;

        ASTGenericParameterType that = (ASTGenericParameterType) o;

        return new EqualsBuilder()
                .append(argument, that.argument)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(argument)
                .toHashCode();
    }

    @Override
    public String toString() {
        return argument.getName();
    }
}
