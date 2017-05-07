package org.androidtransfuse.adapter;

/**
 * @author John Ericksen
 */
public class ASTEnumConstant {

    private final String name;

    public ASTEnumConstant(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
