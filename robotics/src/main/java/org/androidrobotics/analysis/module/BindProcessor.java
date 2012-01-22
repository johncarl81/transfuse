package org.androidrobotics.analysis.module;

import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.InjectionNodeBuilderRepository;
import org.androidrobotics.gen.variableBuilder.VariableInjectionBuilderFactory;

public class BindProcessor extends ClassBindingMethodProcessorAdaptor {

    private InjectionNodeBuilderRepository injectionNodeBuilders;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    public BindProcessor(VariableInjectionBuilderFactory variableInjectionBuilderFactory, InjectionNodeBuilderRepository injectionNodeBuilders) {
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public void innerProcess(ASTType returnType, ASTType annotationValue) {
        injectionNodeBuilders.put(returnType.getName(),
                variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(annotationValue));
    }
}