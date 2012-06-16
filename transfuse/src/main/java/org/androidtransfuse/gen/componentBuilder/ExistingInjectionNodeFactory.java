package org.androidtransfuse.gen.componentBuilder;

import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public class ExistingInjectionNodeFactory implements InjectionNodeFactory {

    private InjectionNode injectionNode;

    public ExistingInjectionNodeFactory(InjectionNode injectionNode) {
        this.injectionNode = injectionNode;
    }

    @Override
    public InjectionNode buildInjectionNode(MethodDescriptor onCreateMethodDescriptor) {
        return injectionNode;
    }
}
