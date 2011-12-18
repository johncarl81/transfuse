package org.androidrobotics.gen.proxy;

import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface DelegateInstantiationGeneratorStrategyFactory {

    DelegateConstructionGeneratorStrategy buildConstructorStrategy(InjectionNode injectionNode);

    DelegateDelayedGeneratorStrategy buildDelayedStrategy(InjectionNode injectionNode);
}
