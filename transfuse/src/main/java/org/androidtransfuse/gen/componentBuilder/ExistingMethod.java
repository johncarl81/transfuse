package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JDefinedClass;

/**
 * @author John Ericksen
 */
public class ExistingMethod implements MethodGenerator {

    private final MethodDescriptor methodDescriptor;

    public ExistingMethod(MethodDescriptor methodDescriptor) {
        this.methodDescriptor = methodDescriptor;
    }

    @Override
    public MethodDescriptor buildMethod(JDefinedClass definedClass) {
        return methodDescriptor;
    }

    @Override
    public void closeMethod(MethodDescriptor methodDescriptor) {
        //none
    }
}
