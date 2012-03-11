package org.androidtransfuse.gen.componentBuilder;

import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface ComponentBuilderFactory {

    OnCreateComponentBuilder buildOnCreateComponentBuilder(InjectionNode injectionNode);

    MethodCallbackGeneratorImpl buildMethodCallbackGenerator(String name, Class<?>... parameterTypes);
}
