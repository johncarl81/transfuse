package org.androidtransfuse.model.r;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author John Ericksen
 */
public class ResourceIdentifier {

    private ASTType rInnerType;
    private String name;

    public ResourceIdentifier(ASTType rInnerType, String name) {
        this.name = name;
        this.rInnerType = rInnerType;
    }

    public String getName() {
        return name;
    }

    public ASTType getRInnerType() {
        return rInnerType;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ResourceIdentifier)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        ResourceIdentifier rhs = (ResourceIdentifier) obj;
        return new EqualsBuilder()
                .append(name, rhs.name)
                .append(rInnerType, rhs.rInnerType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(rInnerType).hashCode();
    }
}
