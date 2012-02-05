package org.androidtransfuse.analysis.adapter;

import com.sun.codemodel.JMod;

/**
 * @author John Ericksen
 */
public enum ASTAccessModifier {

    PUBLIC(JMod.PUBLIC),
    PROTECTED(JMod.PROTECTED),
    PACKAGE_PRIVATE(JMod.NONE),
    PRIVATE(JMod.PRIVATE);

    private int codeModelJMod;

    private ASTAccessModifier(int codeModelJMod) {
        this.codeModelJMod = codeModelJMod;
    }

    public int getCodeModelJMod() {
        return codeModelJMod;
    }
}
