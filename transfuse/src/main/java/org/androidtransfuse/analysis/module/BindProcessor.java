package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;

import javax.inject.Inject;

/**
 * Associates the given return type with the annotated field as a binding.
 *
 * @author John Ericksen
 */
public class BindProcessor extends MethodProcessor {

    private InjectionNodeBuilderRepositoryFactory injectionNodeBuilders;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public BindProcessor(VariableInjectionBuilderFactory variableInjectionBuilderFactory, InjectionNodeBuilderRepositoryFactory injectionNodeBuilders) {
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public void innerProcess(ASTType returnType, ASTType annotationValue) {
        injectionNodeBuilders.putModuleConfig(returnType,
                variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(annotationValue));
    }
}