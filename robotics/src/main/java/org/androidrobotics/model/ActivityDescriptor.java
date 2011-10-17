package org.androidrobotics.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ActivityDescriptor {

    private String name;
    private String activityPackage;
    private int layout;
    //todo: private Map<Class<?>, Collection<String>> delegateMethods = new HashMap<Class<?>, Collection<String>>();
    private List<FieldInjectionPoint> injectionPoint = new ArrayList<FieldInjectionPoint>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    /*public Collection<String> getMethods(Class<?> annotationClass) {
        if (delegateMethods.containsKey(annotationClass)) {
            return delegateMethods.get(annotationClass);
        }
        return Collections.emptySet();
    }

    public void addMethod(Class<?> annotation, String methodName) {
        if (!delegateMethods.containsKey(annotation)) {
            delegateMethods.put(annotation, new HashSet<String>());
        }
        delegateMethods.get(annotation).add(methodName);
    }*/

    public void setPackage(String activityPackage) {
        this.activityPackage = activityPackage;
    }

    public String getPackage() {
        return activityPackage;
    }

    public void addInjectionPoint(FieldInjectionPoint fieldInjectionPoint) {
        injectionPoint.add(fieldInjectionPoint);
    }

    public List<FieldInjectionPoint> getInjectionPoints() {
        return injectionPoint;
    }
}
