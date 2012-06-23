package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ProviderInjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;

import javax.inject.Inject;

public class BindProviderProcessor extends ClassBindingMethodProcessorAdaptor {

    private InjectionNodeBuilderRepositoryFactory injectionNodeBuilders;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository;

    @Inject
    public BindProviderProcessor(InjectionNodeBuilderRepositoryFactory injectionNodeBuilders,
                                 VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                 ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository) {
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.providerInjectionNodeBuilderRepository = providerInjectionNodeBuilderRepository;
    }

    @Override
    public void innerProcess(ASTType returnType, ASTType providerType) {
        injectionNodeBuilders.putModuleConfig(returnType,
                variableInjectionBuilderFactory.buildProviderInjectionNodeBuilder(providerType));

        providerInjectionNodeBuilderRepository.addProvider(returnType,
                variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(providerType));
    }
}