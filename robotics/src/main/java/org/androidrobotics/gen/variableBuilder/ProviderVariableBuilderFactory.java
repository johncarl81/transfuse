package org.androidrobotics.gen.variableBuilder;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public interface ProviderVariableBuilderFactory {

    ProviderInjectionNodeBuilder buildProviderInjectionNodeBuilder(Class<? extends Provider<?>> providerClass);

}
