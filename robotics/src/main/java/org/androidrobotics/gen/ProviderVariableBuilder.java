package org.androidrobotics.gen;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidrobotics.analysis.AnalysisDependencyProcessingCallback;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ProviderVariableBuilder implements VariableBuilder {

    private static final String PROVIDER_METHOD = "get";
    protected static final String PROVIDER_INJECTION_NODE = "provider";

    private Class<? extends Provider<?>> providerClass;
    private ASTClassFactory astClassFactory;

    @Inject
    public ProviderVariableBuilder(@Assisted Class<? extends Provider<?>> providerClass,
                                   ASTClassFactory astClassFactory) {
        this.providerClass = providerClass;
        this.astClassFactory = astClassFactory;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext) {

        InjectionNode providerInjectionNode = injectionBuilderContext.getInjectionNode().getBuilderResource(PROVIDER_INJECTION_NODE);

        if (providerInjectionNode == null) {
            throw new RoboticsAnalysisException("Analysis and build failed to associate provider for " + injectionBuilderContext.getInjectionNode().getClassName());
        }

        InjectionBuilderContext providerInjectionBuilderContext = injectionBuilderContext.buildNextContext(providerInjectionNode);
        JExpression providerVar = providerInjectionBuilderContext.buildVariable();

        return providerVar.invoke(PROVIDER_METHOD);
    }

    @Override
    public InjectionNode processInjectionNode(ASTType astType, AnalysisDependencyProcessingCallback callback) {
        InjectionNode injectionNode = new InjectionNode(astType);

        injectionNode.putBuilderResource(PROVIDER_INJECTION_NODE, callback.processInjectionNode(astClassFactory.buildASTClassType(providerClass)));

        return injectionNode;
    }
}
