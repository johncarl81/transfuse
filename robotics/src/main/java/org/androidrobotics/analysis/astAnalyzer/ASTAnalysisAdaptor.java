package org.androidrobotics.analysis.astAnalyzer;

import org.androidrobotics.analysis.ASTAnalysis;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.adapter.ASTField;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public class ASTAnalysisAdaptor implements ASTAnalysis {
    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {
        //emtpy
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTMethod astMethod, AnalysisContext context) {
        //empty
    }

    @Override
    public void analyzeField(InjectionNode injectionNode, ASTField astField, AnalysisContext context) {
        //empty
    }
}
