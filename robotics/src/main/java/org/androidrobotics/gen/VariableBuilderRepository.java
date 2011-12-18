package org.androidrobotics.gen;

import org.androidrobotics.gen.variableBuilder.VariableBuilder;
import org.androidrobotics.gen.variableBuilder.VariableInjectionBuilderFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class VariableBuilderRepository {

    private Map<String, VariableBuilder> builderMap = new HashMap<String, VariableBuilder>();
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private VariableBuilderRepository parentRepository;

    public VariableBuilderRepository(VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    public VariableBuilderRepository(VariableBuilderRepository parentRepository, VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this(variableInjectionBuilderFactory);

        this.parentRepository = parentRepository;
    }

    public void put(String name, VariableBuilder variableBuilder) {
        builderMap.put(name, variableBuilder);
    }

    public VariableBuilder get(String name) {
        if (!builderMap.containsKey(name)) {
            if (parentRepository != null) {
                return parentRepository.get(name);
            }
            builderMap.put(name, variableInjectionBuilderFactory.buildVariableInjectionBuilder());
        }
        return builderMap.get(name);
    }
}
