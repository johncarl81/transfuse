package org.androidrobotics.model;

import org.androidrobotics.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class IdentifiedInjectionNode extends InjectionNode {

    private Object identifier;

    public IdentifiedInjectionNode(ASTType astType, Object identifier) {
        super(astType);
        this.identifier = identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdentifiedInjectionNode)) return false;
        if (!super.equals(o)) return false;

        IdentifiedInjectionNode that = (IdentifiedInjectionNode) o;

        if (!identifier.equals(that.identifier)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + identifier.hashCode();
        return result;
    }
}
