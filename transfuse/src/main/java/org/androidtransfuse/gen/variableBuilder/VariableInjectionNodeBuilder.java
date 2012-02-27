package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableInjectionNodeBuilder extends InjectionNodeBuilderNoAnnotationAdapter {

    private Analyzer analyzer;

    @Inject
    public VariableInjectionNodeBuilder(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        return analyzer.analyze(astType, astType, context);
    }
}
