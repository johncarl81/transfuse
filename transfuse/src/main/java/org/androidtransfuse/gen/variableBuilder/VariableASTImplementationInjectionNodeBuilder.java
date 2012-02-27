package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableASTImplementationInjectionNodeBuilder extends InjectionNodeBuilderNoAnnotationAdapter {

    private Analyzer analyzer;
    private ASTType implType;

    @Inject
    public VariableASTImplementationInjectionNodeBuilder(@Assisted ASTType implType, Analyzer analyzer) {
        this.analyzer = analyzer;
        this.implType = implType;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        return analyzer.analyze(astType, implType, context);
    }
}
