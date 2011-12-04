package org.androidrobotics.gen;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableBuilderRepositoryFactory {

    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public VariableBuilderRepositoryFactory(VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    public VariableBuilderRepository buildRepository() {
        return new VariableBuilderRepository(variableInjectionBuilderFactory);
    }

    public VariableBuilderRepository buildRepository(VariableBuilderRepository parent) {
        return new VariableBuilderRepository(parent, variableInjectionBuilderFactory);
    }
}
