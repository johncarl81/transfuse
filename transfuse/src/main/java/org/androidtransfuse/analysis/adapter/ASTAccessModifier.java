package org.androidtransfuse.analysis.adapter;

import com.sun.codemodel.JMod;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of available access modifiers, associated with codeModel modifier types
 *
 * @author John Ericksen
 */
public enum ASTAccessModifier {

    PUBLIC(JMod.PUBLIC, Modifier.PUBLIC),
    PROTECTED(JMod.PROTECTED, Modifier.PROTECTED),
    PACKAGE_PRIVATE(JMod.NONE, 0),
    PRIVATE(JMod.PRIVATE, Modifier.PRIVATE);

    private int codeModelJMod;
    private int javaModifier;

    private static Map<Integer, ASTAccessModifier> modifierMap = new HashMap<Integer, ASTAccessModifier>();

    static {
        for (ASTAccessModifier astAccessModifier : values()) {
            modifierMap.put(astAccessModifier.getJavaModifier(), astAccessModifier);
        }
    }

    private ASTAccessModifier(int codeModelJMod, int javaModifier) {
        this.codeModelJMod = codeModelJMod;
        this.javaModifier = javaModifier;
    }

    public int getCodeModelJMod() {
        return codeModelJMod;
    }

    public int getJavaModifier() {
        return javaModifier;
    }

    public static ASTAccessModifier getModifier(int modifier) {
        return modifierMap.get(modifier);
    }
}
