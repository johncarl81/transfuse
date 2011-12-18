package org.androidrobotics.gen.variableBuilder;

import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public class ProviderAspect {
    private InjectionNode providerInjectionNode;

    public ProviderAspect(InjectionNode providerInjectionNode) {
        this.providerInjectionNode = providerInjectionNode;
    }

    public InjectionNode getProviderInjectionNode() {
        return providerInjectionNode;
    }
}
