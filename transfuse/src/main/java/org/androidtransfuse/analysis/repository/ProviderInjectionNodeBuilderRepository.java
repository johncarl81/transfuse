package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class ProviderInjectionNodeBuilderRepository {

    private final Map<ASTType, InjectionNodeBuilder> providerInjectionNodeBuilders = new HashMap<ASTType, InjectionNodeBuilder>();

    public void addProvider(ASTType astType, InjectionNodeBuilder providerInjectionNodeBuilder) {
        providerInjectionNodeBuilders.put(astType, providerInjectionNodeBuilder);
    }

    public InjectionNodeBuilder getProvider(ASTType astType) {
        return providerInjectionNodeBuilders.get(astType);
    }

    public boolean isProviderDefined(ASTType astType) {
        return providerInjectionNodeBuilders.containsKey(astType);
    }
}
