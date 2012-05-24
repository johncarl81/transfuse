package org.androidtransfuse.gen;

import org.androidtransfuse.analysis.adapter.ASTMethod;

/**
 * @author John Ericksen
 */
public class GetterSetterMethodPair {

    private ASTMethod getter;
    private ASTMethod setter;

    public GetterSetterMethodPair(ASTMethod getter, ASTMethod setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public ASTMethod getGetter() {
        return getter;
    }

    public ASTMethod getSetter() {
        return setter;
    }
}
