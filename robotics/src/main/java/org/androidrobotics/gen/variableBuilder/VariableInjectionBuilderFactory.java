package org.androidrobotics.gen.variableBuilder;

import org.androidrobotics.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public interface VariableInjectionBuilderFactory {

    VariableInjectionNodeBuilder buildVariableInjectionBuilder();

    VariableImplementationInjectionNodeBuilder buildVariableInjectionBuilder(Class<?> concreteClass);

    VariableASTImplementationInjectionNodeBuilder buildVariableInjectionNodeBuilder(ASTType astType);

    VariableASTImplementationInjectionBuilder buildVariableInjectionBuilder(ASTType astType);
}
