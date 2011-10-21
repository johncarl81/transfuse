package org.androidrobotics.model;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JType;

/**
 * @author John Ericksen
 */
public class ProviderDescriptor {

    private String builderMethodName;
    private String instanceMethodName;
    private JDefinedClass classDefinition;
    private JType returnType;

    public ProviderDescriptor(JDefinedClass classDefinition, String instanceMethodName, JType returnType, String builderMethodName) {
        this.builderMethodName = builderMethodName;
        this.instanceMethodName = instanceMethodName;
        this.classDefinition = classDefinition;
        this.returnType = returnType;
    }

    public String getInstanceMethodName() {
        return instanceMethodName;
    }

    public String getBuilderMethodName() {
        return builderMethodName;
    }

    public JDefinedClass getClassDefinition() {
        return classDefinition;
    }

    public JType getReturnType() {
        return returnType;
    }
}
