package org.androidrobotics.gen;

import java.util.*;

/**
 * @author John Ericksen
 */
public class ActivityDescriptor {

    private String name;
    private int layout;
    private String delegateClass;
    private Map<Class<?>, Collection<String>> delegateMethods = new HashMap<Class<?>, Collection<String>>();

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

    public Collection<String> getMethods(Class<?> annotationClass) {
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
    }

    public String getDelegateClass() {
        return delegateClass;
    }

    public void setDelegateClass(String delegateClass) {
        this.delegateClass = delegateClass;
    }

    public String getShorDelegateClassName() {
        return delegateClass.substring(delegateClass.lastIndexOf(".") + 1);
    }
}
