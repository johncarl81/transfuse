package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface AnalysisDependencyProcessingCallback {

    InjectionNode processInjectionNode(ASTType astType);
}
