package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.ProviderInjectionNodeBuilderRepository;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class GeneratedProviderInjectionNodeBuilder implements InjectionNodeBuilder {

    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository;
    private Provider<VariableInjectionBuilder> variableInjectionBuilderProvider;
    private InjectionPointFactory injectionPointFactory;

    @Inject
    public GeneratedProviderInjectionNodeBuilder(VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                                 ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository,
                                                 Provider<VariableInjectionBuilder> variableInjectionBuilderProvider,
                                                 InjectionPointFactory injectionPointFactory) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.providerInjectionNodeBuilderRepository = providerInjectionNodeBuilderRepository;
        this.variableInjectionBuilderProvider = variableInjectionBuilderProvider;
        this.injectionPointFactory = injectionPointFactory;
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
        InjectionNode providerInjectionNode = injectionPointFactory.buildInjectionNode(providerGenericType, context);
        providerInjectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildGeneratedProviderVariableBuilder(providerInjectionNode));

        return injectionNode;
    }

    private ASTType getProviderTemplateType(ASTType astType) {
        return astType.getGenericParameters().get(0);
    }
}
