package org.androidrobotics.analysis.module;

import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.InjectionNodeBuilderRepository;
import org.androidrobotics.gen.variableBuilder.VariableInjectionBuilderFactory;

public class BindProviderProcessor extends ClassBindingMethodProcessorAdaptor {

    private InjectionNodeBuilderRepository injectionNodeBuilders;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    public BindProviderProcessor(VariableInjectionBuilderFactory variableInjectionBuilderFactory, InjectionNodeBuilderRepository injectionNodeBuilders) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.injectionNodeBuilders = injectionNodeBuilders;
    }

    @Override
    public void innerProcess(ASTType returnType, ASTType annotationValue) {
        injectionNodeBuilders.put(returnType.getName(),
                variableInjectionBuilderFactory.buildProviderInjectionNodeBuilder(annotationValue));
    }
}