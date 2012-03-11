package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JDefinedClass;

public interface MethodGenerator {

    MethodDescriptor buildMethod(JDefinedClass definedClass);

    void closeMethod(MethodDescriptor methodDescriptor);
}