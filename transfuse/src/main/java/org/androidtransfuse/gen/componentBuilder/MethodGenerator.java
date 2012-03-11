package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;

public interface MethodGenerator {

    JMethod buildMethod(JDefinedClass definedClass);

    void closeMethod(JMethod method);
}