package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.ProviderGenerator;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResource;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class GeneratedProviderVariableBuilder implements VariableBuilder {

    private ProviderGenerator providerGenerator;
    private UniqueVariableNamer variableNamer;
    private InjectionNode providerTypeInjectionNode;

    @Inject
    public GeneratedProviderVariableBuilder(@Assisted InjectionNode providerTypeInjectionNode,
                                            ProviderGenerator providerGenerator,
                                            UniqueVariableNamer variableNamer) {
        this.providerGenerator = providerGenerator;
        this.variableNamer = variableNamer;
        this.providerTypeInjectionNode = providerTypeInjectionNode;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {

        JDefinedClass providerClass = generateProviderType(providerTypeInjectionNode, injectionBuilderContext.getRResource());

        JFieldVar providerField = injectionBuilderContext.getDefinedClass().field(JMod.PRIVATE, providerClass, variableNamer.generateName(providerClass.fullName()));

        injectionBuilderContext.getBlock().assign(providerField, JExpr._new(providerClass));

        return providerField;
    }

    private JDefinedClass generateProviderType(InjectionNode providerTypeInjectionNode, RResource rResource) {

        return providerGenerator.generateProvider(providerTypeInjectionNode, rResource);
    }
}
