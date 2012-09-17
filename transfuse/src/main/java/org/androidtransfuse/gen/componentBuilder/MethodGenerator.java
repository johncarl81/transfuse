package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.model.MethodDescriptor;

public interface MethodGenerator {

    MethodDescriptor buildMethod(JDefinedClass definedClass);

    void closeMethod(MethodDescriptor methodDescriptor);
}