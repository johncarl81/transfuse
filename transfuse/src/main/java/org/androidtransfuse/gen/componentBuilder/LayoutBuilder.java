package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JBlock;
import org.androidtransfuse.model.r.RResource;

/**
 * @author John Ericksen
 */
public interface LayoutBuilder {
    void buildLayoutCall(JBlock block, RResource rResource);
}
