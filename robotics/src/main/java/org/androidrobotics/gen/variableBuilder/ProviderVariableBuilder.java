package org.androidrobotics.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.Analyzer;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ProviderVariableBuilder implements VariableBuilder {

    private static final String PROVIDER_METHOD = "get";
    public static final String PROVIDER_INJECTION_NODE = "provider";

    private Class<? extends Provider<?>> providerClass;
    private ASTClassFactory astClassFactory;
    private Analyzer analyzer;

    @Inject
    public ProviderVariableBuilder(@Assisted Class<? extends Provider<?>> providerClass,
                                   Analyzer analyzer,
                                   ASTClassFactory astClassFactory) {
        this.providerClass = providerClass;
        this.astClassFactory = astClassFactory;
        this.analyzer = analyzer;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {

        InjectionNode providerInjectionNode = injectionNode.getBuilderResource(PROVIDER_INJECTION_NODE);

        if (providerInjectionNode == null) {
            throw new RoboticsAnalysisException("Analysis and build failed to associate provider for " + injectionNode.getClassName());
        }

        JExpression providerVar = injectionBuilderContext.buildVariable(providerInjectionNode);

        return providerVar.invoke(PROVIDER_METHOD);
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        InjectionNode injectionNode = new InjectionNode(astType);

        ASTType providerType = astClassFactory.buildASTClassType(providerClass);

        injectionNode.putBuilderResource(PROVIDER_INJECTION_NODE, analyzer.analyze(providerType, providerType, context));

        return injectionNode;
    }
}
