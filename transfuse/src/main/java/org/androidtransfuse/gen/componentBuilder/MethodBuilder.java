package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JDefinedClass;

/**
 * @author John Ericksen
 */
public interface MethodBuilder {

    MethodDescriptor buildMethod(JDefinedClass definedClass);

}
