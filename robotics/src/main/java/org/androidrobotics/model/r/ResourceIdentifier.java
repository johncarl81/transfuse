package org.androidrobotics.model.r;

import org.androidrobotics.analysis.adapter.ASTType;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceIdentifier)) {
            return false;
        }

        ResourceIdentifier that = (ResourceIdentifier) o;

        if (!name.equals(that.name)) {
            return false;
        }
        if (!rInnerType.equals(that.rInnerType)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = rInnerType.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
