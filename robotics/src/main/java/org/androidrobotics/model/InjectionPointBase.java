package org.androidrobotics.model;

import org.androidrobotics.analysis.InjectionNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public abstract class InjectionPointBase {

    private String name;
    private List<InjectionNode> injectionNodes = new ArrayList<InjectionNode>();

    public InjectionPointBase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addInjectionNode(InjectionNode injectionNode) {
        this.injectionNodes.add(injectionNode);
    }

    public List<InjectionNode> getInjectionNodes() {
        return injectionNodes;
    }
}
