package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface AnalysisDependencyProcessingCallback {

    InjectionNode processInjectionNode(ASTType astType);
}
