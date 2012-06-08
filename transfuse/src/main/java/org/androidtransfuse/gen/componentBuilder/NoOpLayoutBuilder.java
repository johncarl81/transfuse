package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;

/**
 * @author John Ericksen
 */
public class NoOpLayoutBuilder implements LayoutBuilder {
    @Override
    public void buildLayoutCall(JDefinedClass definedClass, JBlock block) {
        //noop
    }
}
