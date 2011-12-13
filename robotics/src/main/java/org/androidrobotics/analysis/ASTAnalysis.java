package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.ASTField;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface ASTAnalysis {

    void analyzeType(InjectionNode injectionNode, ASTType astType, AnalysisContext context);

    void analyzeMethod(InjectionNode injectionNode, ASTMethod astMethod, AnalysisContext context);

    void analyzeField(InjectionNode injectionNode, ASTField astField, AnalysisContext context);
}
