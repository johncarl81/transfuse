package org.androidtransfuse.gen;

import com.sun.codemodel.JDefinedClass;

/**
 * @author John Ericksen
 */
public interface ComponentBuilder {

    void build(JDefinedClass definedClass, ComponentDescriptor descriptor);
}
