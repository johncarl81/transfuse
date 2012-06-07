package org.androidtransfuse.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public abstract class MethodInjectionPointBase {

    private List<InjectionNode> injectionNodes = new ArrayList<InjectionNode>();

    public void addInjectionNode(InjectionNode injectionNode) {
        this.injectionNodes.add(injectionNode);
    }

    public List<InjectionNode> getInjectionNodes() {
        return injectionNodes;
    }
}
