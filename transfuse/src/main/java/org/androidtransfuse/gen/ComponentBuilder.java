package org.androidtransfuse.gen;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.model.r.RResource;

/**
 * @author John Ericksen
 */
public interface ComponentBuilder {

    void build(JDefinedClass definedClass, RResource rResource);
}
