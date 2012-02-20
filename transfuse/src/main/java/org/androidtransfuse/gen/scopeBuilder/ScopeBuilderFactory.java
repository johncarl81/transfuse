package org.androidtransfuse.gen.scopeBuilder;

import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface ScopeBuilderFactory {

    SingletonScopeBuilder buildSingletonScopeBuilder(InjectionNode applicationInjectionNode);
}
