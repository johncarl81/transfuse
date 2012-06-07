package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;

import javax.inject.Inject;

public class BindProcessor extends ClassBindingMethodProcessorAdaptor {

    private InjectionNodeBuilderRepository injectionNodeBuilders;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
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