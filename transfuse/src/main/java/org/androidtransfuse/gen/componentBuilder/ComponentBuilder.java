package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.model.ComponentDescriptor;

/**
 * @author John Ericksen
 */
public interface ComponentBuilder {

    void build(JDefinedClass definedClass, ComponentDescriptor descriptor);
}
