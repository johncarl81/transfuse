package org.androidrobotics.model.r;

import org.androidrobotics.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class ResourceIdentifier {

    private ASTType rType;
    private String resourceType;
    private String name;

    public ResourceIdentifier(ASTType rType, String resourceType, String name) {
        this.resourceType = resourceType;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceIdentifier)) return false;

        ResourceIdentifier that = (ResourceIdentifier) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (resourceType != null ? !resourceType.equals(that.resourceType) : that.resourceType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = resourceType != null ? resourceType.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public ASTType getRType() {
        return rType;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getName() {
        return name;
    }
}
