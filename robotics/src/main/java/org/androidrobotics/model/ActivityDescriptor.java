package org.androidrobotics.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ActivityDescriptor {

    private PackageClass packageClass;
    private int layout;
    private List<FieldInjectionPoint> injectionPoints = new ArrayList<FieldInjectionPoint>();

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

    public void addInjectionPoint(FieldInjectionPoint fieldInjectionPoint) {
        injectionPoints.add(fieldInjectionPoint);
    }

    public List<FieldInjectionPoint> getInjectionPoints() {
        return injectionPoints;
    }
}
