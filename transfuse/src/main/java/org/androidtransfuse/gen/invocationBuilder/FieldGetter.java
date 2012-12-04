package org.androidtransfuse.gen.invocationBuilder;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class FieldGetter {
    private final ASTType returnType;
    private final ASTType variableType;
    private final String name;

    public FieldGetter(ASTType returnType, ASTType variableType, String name) {
        this.returnType = returnType;
        this.variableType = variableType;
        this.name = name;
    }

    public ASTType getReturnType() {
        return returnType;
    }

    public ASTType getVariableType() {
        return variableType;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FieldGetter)) {
            return false;
        }
        FieldGetter that = (FieldGetter) o;
        return new EqualsBuilder().append(name, that.name).append(variableType, that.variableType).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(variableType).hashCode();
    }
}