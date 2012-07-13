package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.gen.scopeBuilder.ScopeAspectFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ScopeAspectFactoryRepository {

    private Map<Class<? extends Annotation>, ScopeAspectFactory> scopeVariableBuilderMap = new HashMap<Class<? extends Annotation>, ScopeAspectFactory>();

    public void putAspectFactory(Class<? extends Annotation> scopeType, ScopeAspectFactory scopeAspectFactory) {
        scopeVariableBuilderMap.put(scopeType, scopeAspectFactory);
    }

    public Set<Class<? extends Annotation>> getScopes() {
        return scopeVariableBuilderMap.keySet();
    }

    public ScopeAspectFactory getScopeAspectFactory(Class<? extends Annotation> scopeType) {
        return scopeVariableBuilderMap.get(scopeType);
    }
}
