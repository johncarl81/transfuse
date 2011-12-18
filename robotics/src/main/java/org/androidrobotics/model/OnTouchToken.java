package org.androidrobotics.model;

import org.androidrobotics.analysis.adapter.ASTMethod;

/**
 * @author John Ericksen
 */
public class OnTouchToken {

    private ASTMethod astMethod;

    public OnTouchToken(ASTMethod astMethod) {
        this.astMethod = astMethod;
    }

    public ASTMethod getAstMethod() {
        return astMethod;
    }
}
