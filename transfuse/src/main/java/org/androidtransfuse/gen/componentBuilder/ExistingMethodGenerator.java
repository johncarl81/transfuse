package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JDefinedClass;

/**
 * @author John Ericksen
 */
public class ExistingMethodGenerator implements MethodGenerator {

    private MethodDescriptor methodDescriptor;

    public ExistingMethodGenerator(MethodDescriptor methodDescriptor) {
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
