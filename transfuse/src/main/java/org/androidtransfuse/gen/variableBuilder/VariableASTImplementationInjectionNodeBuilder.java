package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class VariableASTImplementationInjectionNodeBuilder extends InjectionNodeBuilderNoAnnotationAdapter {

    private Analyzer analyzer;
    private ASTType implType;
    private Provider<VariableInjectionBuilder> variableInjectionBuilderProvider;

    @Inject
    public VariableASTImplementationInjectionNodeBuilder(@Assisted ASTType implType,
                                                         Analyzer analyzer,
                                                         Provider<VariableInjectionBuilder> variableInjectionBuilderProvider) {
        this.analyzer = analyzer;
        this.implType = implType;
        this.variableInjectionBuilderProvider = variableInjectionBuilderProvider;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        InjectionNode injectionNode = analyzer.analyze(astType, implType, context);

        //default variable builder
        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());

        return injectionNode;
    }
}
