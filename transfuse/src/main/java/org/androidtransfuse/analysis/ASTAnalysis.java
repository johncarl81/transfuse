package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface ASTAnalysis {

    void analyzeType(InjectionNode injectionNode, ASTType astType, AnalysisContext context);

    void analyzeMethod(InjectionNode injectionNode, ASTMethod astMethod, AnalysisContext context);

    void analyzeField(InjectionNode injectionNode, ASTField astField, AnalysisContext context);
}
