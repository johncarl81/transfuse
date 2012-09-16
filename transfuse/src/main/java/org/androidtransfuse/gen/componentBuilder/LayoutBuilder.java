package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;

/**
 * @author John Ericksen
 */
public interface LayoutBuilder {

    void buildLayoutCall(JDefinedClass definedClass, JBlock block);
}
