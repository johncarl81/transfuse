package org.androidtransfuse.model;

import org.androidtransfuse.analysis.adapter.ASTMethod;

/**
 * @author John Ericksen
 */
public class GetterSetterMethodPair {

    private final String name;
    private final ASTMethod getter;
    private final ASTMethod setter;

    public GetterSetterMethodPair(String name, ASTMethod getter, ASTMethod setter) {
        this.name = name;
        this.getter = getter;
        this.setter = setter;
    }

    public ASTMethod getGetter() {
        return getter;
    }

    public ASTMethod getSetter() {
        return setter;
    }

    public String getName() {
        return name;
    }
}
