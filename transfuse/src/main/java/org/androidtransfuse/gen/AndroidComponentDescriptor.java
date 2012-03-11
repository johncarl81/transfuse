package org.androidtransfuse.gen;

import org.androidtransfuse.model.PackageClass;

import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class AndroidComponentDescriptor {

    private PackageClass packageClass;
    private String type;
    private Set<ComponentBuilder> componentBuilders = new HashSet<ComponentBuilder>();

    public AndroidComponentDescriptor(String type, PackageClass packageClass) {
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
