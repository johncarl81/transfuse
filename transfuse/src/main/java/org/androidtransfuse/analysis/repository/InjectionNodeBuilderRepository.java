package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionNodeBuilder;

import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionNodeBuilderRepository {

    private Map<String, InjectionNodeBuilder> builderMap = new HashMap<String, InjectionNodeBuilder>();
    private Provider<VariableInjectionNodeBuilder> variableInjectionNodeBuilderProvider;
    private InjectionNodeBuilderRepository parentRepository;

    public InjectionNodeBuilderRepository(Provider<VariableInjectionNodeBuilder> variableInjectionNodeBuilderProvider) {
        this.variableInjectionNodeBuilderProvider = variableInjectionNodeBuilderProvider;
    }

    public InjectionNodeBuilderRepository(InjectionNodeBuilderRepository parentRepository,
                                          Provider<VariableInjectionNodeBuilder> variableInjectionNodeBuilderProvider) {
        this(variableInjectionNodeBuilderProvider);

        this.parentRepository = parentRepository;
    }

    public void put(String name, InjectionNodeBuilder variableBuilder) {
        builderMap.put(name, variableBuilder);
    }

    public InjectionNodeBuilder get(String name) {
        if (!builderMap.containsKey(name)) {
            if (parentRepository != null) {
                return parentRepository.get(name);
            }
            builderMap.put(name, variableInjectionNodeBuilderProvider.get());
        }
        return builderMap.get(name);
    }
}
