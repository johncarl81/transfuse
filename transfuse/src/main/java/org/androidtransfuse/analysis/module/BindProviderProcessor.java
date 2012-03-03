package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ProviderInjectionNodeBuilderRepository;
import org.androidtransfuse.gen.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;

import javax.inject.Inject;

public class BindProviderProcessor extends ClassBindingMethodProcessorAdaptor {

    private InjectionNodeBuilderRepository injectionNodeBuilders;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository;

    @Inject
    public BindProviderProcessor(VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                 InjectionNodeBuilderRepository injectionNodeBuilders,
                                 ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.providerInjectionNodeBuilderRepository = providerInjectionNodeBuilderRepository;
    }

    @Override
    public void innerProcess(ASTType returnType, ASTType providerType) {
        injectionNodeBuilders.put(returnType.getName(),
                variableInjectionBuilderFactory.buildProviderInjectionNodeBuilder(providerType));

        providerInjectionNodeBuilderRepository.addProvider(returnType,
                variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(providerType));
    }
}