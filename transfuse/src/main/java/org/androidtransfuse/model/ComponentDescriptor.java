package org.androidtransfuse.model;

import org.androidtransfuse.gen.componentBuilder.ComponentBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ComponentDescriptor {

    private PackageClass packageClass;
    private String type;
    private Set<ComponentBuilder> componentBuilders = new HashSet<ComponentBuilder>();

    public ComponentDescriptor(String type, PackageClass packageClass) {
        this.type = type;
        this.packageClass = packageClass;
    }

    public PackageClass getPackageClass() {
        return packageClass;
    }

    public String getType() {
        return type;
    }

    public Set<ComponentBuilder> getComponentBuilders() {
        return componentBuilders;
    }
}
