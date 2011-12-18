package org.androidrobotics.gen.variableBuilder;

import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.VariableImplementationInjectionBuilder;

/**
 * @author John Ericksen
 */
public interface VariableInjectionBuilderFactory {

    VariableInjectionBuilder buildVariableInjectionBuilder();

    VariableImplementationInjectionBuilder buildVariableInjectionBuilder(Class<?> concreteClass);

    VariableASTImplementationInjectionBuilder buildVariableInjectionBuilder(ASTType astType);
}
