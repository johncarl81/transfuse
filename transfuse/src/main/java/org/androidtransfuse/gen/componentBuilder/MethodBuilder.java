package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.model.MethodDescriptor;

/**
 * @author John Ericksen
 */
public interface MethodBuilder {

    MethodDescriptor buildMethod(JDefinedClass definedClass);

    void closeMethod(MethodDescriptor descriptor);

}
