package org.androidrobotics.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.Analyzer;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ProviderInjectionNodeBuilder implements InjectionNodeBuilder {

    private ASTClassFactory astClassFactory;
    private Class<? extends Provider<?>> providerClass;
    private Analyzer analyzer;

    @Inject
    public ProviderInjectionNodeBuilder(@Assisted Class<? extends Provider<?>> providerClass,
                                        ASTClassFactory astClassFactory,
                                        Analyzer analyzer) {
        this.astClassFactory = astClassFactory;
        this.providerClass = providerClass;
        this.analyzer = analyzer;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        InjectionNode injectionNode = new InjectionNode(astType);

        ASTType providerType = astClassFactory.buildASTClassType(providerClass);

        injectionNode.addAspect(VariableBuilder.class, new ProviderVariableBuilder(analyzer.analyze(providerType, providerType, context)));

        return injectionNode;
    }
}
