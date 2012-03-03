package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class GeneratedProviderInjectionNodeBuilder implements InjectionNodeBuilder {

    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private Analyzer analyzer;

    @Inject
    public GeneratedProviderInjectionNodeBuilder(VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                                 Analyzer analyzer) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.analyzer = analyzer;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, Collection<ASTAnnotation> annotations) {
        InjectionNode injectionNode = new InjectionNode(astType);

        ASTType tempType = getProviderTemplateType(astType);
        InjectionNode tempInjectionNode = analyzer.analyze(tempType, tempType, context);

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildGeneratedProviderVariableBuilder(tempInjectionNode));

        return injectionNode;
    }

    private ASTType getProviderTemplateType(ASTType astType) {
        return astType.getGenericParameters().get(0);
    }
}
