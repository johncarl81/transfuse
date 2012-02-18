package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ProviderInjectionNodeBuilder implements InjectionNodeBuilder {

    private ASTType providerType;
    private Analyzer analyzer;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public ProviderInjectionNodeBuilder(@Assisted ASTType providerType,
                                        Analyzer analyzer,
                                        VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.providerType = providerType;
        this.analyzer = analyzer;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        InjectionNode injectionNode = new InjectionNode(astType);

        InjectionNode providerInjectionNode = analyzer.analyze(providerType, providerType, context);

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildProviderVariableBuilder(providerInjectionNode));

        return injectionNode;
    }
}
