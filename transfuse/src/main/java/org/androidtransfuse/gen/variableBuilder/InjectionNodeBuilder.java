package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface InjectionNodeBuilder {

    InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation);
}
