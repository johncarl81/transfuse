package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * Module processing class for handling Method annotations.  Pulls the return type and annotation value field
 * and supplies them to the abstract innerProcess method.
 *
 * @author John Ericksen
 */
public interface MethodProcessor {

    ModuleConfiguration process(ASTType moduleType, ASTMethod astMethod, ASTAnnotation astAnnotation);
}