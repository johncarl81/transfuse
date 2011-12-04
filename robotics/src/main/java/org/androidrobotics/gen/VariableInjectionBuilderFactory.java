package org.androidrobotics.gen;

import org.androidrobotics.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public interface VariableInjectionBuilderFactory {

    VariableInjectionBuilder buildVariableInjectionBuilder();

    VariableImplementationInjectionBuilder buildVariableInjectionBuilder(Class<?> concreteClass);

    VariableASTImplementationInjectionBuilder buildVariableInjectionBuilder(ASTType astType);
}
