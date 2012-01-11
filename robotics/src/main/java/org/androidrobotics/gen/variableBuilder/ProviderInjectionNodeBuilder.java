package org.androidrobotics.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.Analyzer;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ProviderInjectionNodeBuilder implements InjectionNodeBuilder {

    private ASTType providerType;
    private Analyzer analyzer;

    @Inject
    public ProviderInjectionNodeBuilder(@Assisted ASTType providerType,
                                        Analyzer analyzer) {
        this.providerType = providerType;
        this.analyzer = analyzer;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        InjectionNode injectionNode = new InjectionNode(astType);

        injectionNode.addAspect(VariableBuilder.class, new ProviderVariableBuilder(analyzer.analyze(providerType, providerType, context)));

        return injectionNode;
    }
}
