package org.androidrobotics.model;

import com.sun.codemodel.JDefinedClass;

/**
 * @author John Ericksen
 */
public class ProxyDescriptor {

    JDefinedClass definedClass;

    public ProxyDescriptor(JDefinedClass definedClass) {
        this.definedClass = definedClass;
    }

    public JDefinedClass getClassDefinition() {
        return definedClass;
    }
}
