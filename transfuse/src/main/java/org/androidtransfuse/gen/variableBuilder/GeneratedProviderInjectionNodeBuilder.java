package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ProviderInjectionNodeBuilderRepository;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class GeneratedProviderInjectionNodeBuilder implements InjectionNodeBuilder {

    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository;
    private Analyzer analyzer;

    @Inject
    public GeneratedProviderInjectionNodeBuilder(VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                                 Analyzer analyzer, ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.analyzer = analyzer;
        this.providerInjectionNodeBuilderRepository = providerInjectionNodeBuilderRepository;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, Collection<ASTAnnotation> annotations) {

        ASTType providerGenericType = getProviderTemplateType(astType);

        if (providerInjectionNodeBuilderRepository.isProviderDefined(providerGenericType)) {
            //already defined
            InjectionNodeBuilder providerInjectionNodeBuilder = providerInjectionNodeBuilderRepository.getProvider(providerGenericType);

            return providerInjectionNodeBuilder.buildInjectionNode(astType, context, annotations);
        }

        InjectionNode injectionNode = new InjectionNode(astType);
        InjectionNode providerInjectionNode = analyzer.analyze(providerGenericType, providerGenericType, context);

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildGeneratedProviderVariableBuilder(providerInjectionNode));

        return injectionNode;
    }

    private ASTType getProviderTemplateType(ASTType astType) {
        return astType.getGenericParameters().get(0);
    }
}
