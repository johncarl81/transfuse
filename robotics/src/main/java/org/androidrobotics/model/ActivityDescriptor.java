package org.androidrobotics.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ActivityDescriptor {

    private PackageClass packageClass;
    private int layout;
    private List<InjectionNode> injectionNodes = new ArrayList<InjectionNode>();

    public PackageClass getPackageClass() {
        return packageClass;
    }

    public void setPackageClass(PackageClass packageClass) {
        this.packageClass = packageClass;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public void addInjectionNode(InjectionNode injectionNode) {
        injectionNodes.add(injectionNode);
    }

    public List<InjectionNode> getInjectionNodes() {
        return injectionNodes;
    }
}
