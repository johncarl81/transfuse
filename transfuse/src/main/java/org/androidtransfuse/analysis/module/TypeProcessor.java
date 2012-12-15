package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;

/**
 * @author John Ericksen
 */
public interface TypeProcessor {

    ModuleConfiguration process(ASTAnnotation typeAnnotation);
}
