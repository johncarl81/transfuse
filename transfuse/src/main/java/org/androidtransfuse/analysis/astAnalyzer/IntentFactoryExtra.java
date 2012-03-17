package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class IntentFactoryExtra implements Comparable<IntentFactoryExtra> {

    private boolean required;
    private String name;
    private ASTType type;

    public IntentFactoryExtra(boolean required, String name, ASTType type) {
        this.required = required;
        this.name = name;
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public String getName() {
        //todo: require simple extra names for Intent Factory parameters
        return name;
    }

    public ASTType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntentFactoryExtra)) return false;

        IntentFactoryExtra that = (IntentFactoryExtra) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(IntentFactoryExtra intentFactoryExtra) {
        return getName().compareTo(intentFactoryExtra.getName());
    }
}
