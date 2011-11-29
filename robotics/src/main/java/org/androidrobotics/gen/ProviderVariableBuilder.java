package org.androidrobotics.gen;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidrobotics.analysis.TypeInjectionAnalyzer;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ProviderVariableBuilder implements VariableBuilder {

    private static final String PROVIDER_METHOD = "get";

    private Class<? extends Provider<?>> providerClass;
    private ASTClassFactory astClassFactory;
    private TypeInjectionAnalyzer typeInjectionAnalyzer;

    @Inject
    public ProviderVariableBuilder(@Assisted Class<? extends Provider<?>> providerClass,
                                   ASTClassFactory astClassFactory,
                                   TypeInjectionAnalyzer typeInjectionAnalyzer) {
        this.providerClass = providerClass;
        this.astClassFactory = astClassFactory;
        this.typeInjectionAnalyzer = typeInjectionAnalyzer;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext) {

        InjectionNode providerInjectionNode = typeInjectionAnalyzer.analyze(astClassFactory.buildASTClassType(providerClass));

        InjectionBuilderContext providerInjectionBuilderContext = injectionBuilderContext.buildNextContext(providerInjectionNode);
        JExpression providerVar = providerInjectionBuilderContext.buildVariable();

        return providerVar.invoke(PROVIDER_METHOD);
    }
}
