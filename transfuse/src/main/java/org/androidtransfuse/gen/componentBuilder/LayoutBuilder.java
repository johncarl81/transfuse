package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.model.r.RResource;

/**
 * @author John Ericksen
 */
public interface LayoutBuilder {
    void buildLayoutCall(JDefinedClass definedClass, JBlock block, RResource rResource);
}