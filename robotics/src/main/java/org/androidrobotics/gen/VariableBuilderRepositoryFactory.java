package org.androidrobotics.gen;

import org.androidrobotics.gen.variableBuilder.VariableInjectionNodeBuilder;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class VariableBuilderRepositoryFactory {

    private Provider<VariableInjectionNodeBuilder> variableInjectionNodeBuilderProvider;

    @Inject
    public VariableBuilderRepositoryFactory(Provider<VariableInjectionNodeBuilder> variableInjectionNodeBuilderProvider) {
        this.variableInjectionNodeBuilderProvider = variableInjectionNodeBuilderProvider;
    }

    public InjectionNodeBuilderRepository buildRepository() {
        return new InjectionNodeBuilderRepository(variableInjectionNodeBuilderProvider);
    }

    public InjectionNodeBuilderRepository buildRepository(InjectionNodeBuilderRepository parent) {
        return new InjectionNodeBuilderRepository(parent, variableInjectionNodeBuilderProvider);
    }
}
