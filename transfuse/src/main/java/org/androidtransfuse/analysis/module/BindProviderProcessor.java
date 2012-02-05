package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;

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