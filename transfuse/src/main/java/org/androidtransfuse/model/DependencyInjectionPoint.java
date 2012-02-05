package org.androidtransfuse.model;

/**
 * @author John Ericksen
 */
public class DependencyInjectionPoint {

    private InjectionNode injectionNode;

    public DependencyInjectionPoint(InjectionNode injectionNode) {
        this.injectionNode = injectionNode;
    }

    public InjectionNode getInjectionNode() {
        return injectionNode;
    }
}
