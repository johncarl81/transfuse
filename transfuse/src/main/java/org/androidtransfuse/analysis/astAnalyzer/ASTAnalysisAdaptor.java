package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

/**
 * Adaptor to allow subclasses to not be required to implement all analysis methods
 *
 * @author John Ericksen
 */
public class ASTAnalysisAdaptor implements ASTAnalysis {
    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {
        //emtpy
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTType concreteType, ASTMethod astMethod, AnalysisContext context) {
        //empty
    }

    @Override
    public void analyzeField(InjectionNode injectionNode, ASTType concreteType, ASTField astField, AnalysisContext context) {
        //empty
    }
}
