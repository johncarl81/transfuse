package org.androidtransfuse.model;

import com.sun.codemodel.JMethod;

/**
 * @author John Ericksen
 */
public class SingletonDescriptor {

    private JMethod constructor;
    private JMethod getInstanceMethod;

    public SingletonDescriptor(JMethod constructor, JMethod getInstanceMethod) {
        this.constructor = constructor;
        this.getInstanceMethod = getInstanceMethod;
    }

    public JMethod getConstructor() {
        return constructor;
    }

    public JMethod getGetInstanceMethod() {
        return getInstanceMethod;
    }
}
