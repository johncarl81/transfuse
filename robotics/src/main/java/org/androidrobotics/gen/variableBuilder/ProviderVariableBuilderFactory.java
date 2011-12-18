package org.androidrobotics.gen.variableBuilder;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public interface ProviderVariableBuilderFactory {

    ProviderVariableBuilder buildProviderVariableBuilder(Class<? extends Provider<?>> providerClass);

}
