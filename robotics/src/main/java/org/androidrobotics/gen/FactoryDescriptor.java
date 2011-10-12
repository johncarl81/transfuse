package org.androidrobotics.gen;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JType;

/**
 * @author John Ericksen
 */
public class FactoryDescriptor {

    private String builderMethodName;
    private String instanceMethodName;
    private JDefinedClass classDefinition;
    private JType returnType;

    public FactoryDescriptor(JDefinedClass classDefinition, String instanceMethodName, JType returnType, String builderMethodName) {
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
