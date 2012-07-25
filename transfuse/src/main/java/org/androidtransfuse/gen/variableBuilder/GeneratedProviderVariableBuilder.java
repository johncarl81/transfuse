package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.ProviderGenerator;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class GeneratedProviderVariableBuilder extends ConsistentTypeVariableBuilder {

    private ProviderGenerator providerGenerator;
    private UniqueVariableNamer variableNamer;
    private InjectionNode providerTypeInjectionNode;

    @Inject
    public GeneratedProviderVariableBuilder(@Assisted InjectionNode providerTypeInjectionNode,
                                            ProviderGenerator providerGenerator,
                                            UniqueVariableNamer variableNamer,
                                            TypedExpressionFactory typedExpressionFactory) {
        super(Provider.class, typedExpressionFactory);
        this.providerGenerator = providerGenerator;
        this.variableNamer = variableNamer;
        this.providerTypeInjectionNode = providerTypeInjectionNode;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {

        JDefinedClass providerClass = generateProviderType(providerTypeInjectionNode);

        return injectionBuilderContext.getBlock().decl(providerClass, variableNamer.generateName(providerClass),
                JExpr._new(providerClass));
    }

    private JDefinedClass generateProviderType(InjectionNode providerTypeInjectionNode) {

        return providerGenerator.generateProvider(providerTypeInjectionNode);
    }
}
