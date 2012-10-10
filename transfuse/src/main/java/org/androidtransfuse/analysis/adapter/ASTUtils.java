package org.androidtransfuse.analysis.adapter;

import java.util.Collection;

/**
 * Utility singleton for AST classes.
 *
 * @author John Ericksen
 */
public final class ASTUtils {

    private static final ASTUtils INSTANCE = new ASTUtils();

    private ASTUtils() {
        //private singleton constructor
    }

    public static ASTUtils getInstance() {
        return INSTANCE;
    }

    /**
     * Determines if the given ASTType inherits or extends from the given inheritable ASTType
     *
     * @param astType     target
     * @param inheritable inheritance target
     * @param implement flag to trigger the method to search for implements inheritance
     * @param extend flag to trigger the method to search for extends inheritance
     * @return true if the given astType target inherits from the inheritable type with the given rules.
     */
    public boolean inherits(ASTType astType, ASTType inheritable, boolean implement, boolean extend) {
        if (astType == null) {
            return false;
        }
        if (astType.equals(inheritable)) {
            return true;
        }
        if (implement) {
            for (ASTType typeInterfaces : astType.getInterfaces()) {
                if (inherits(typeInterfaces, inheritable, implement, extend)) {
                    return true;
                }
            }
        }
        return extend && inherits(astType.getSuperClass(), inheritable, implement, extend);
    }

    public ASTAnnotation getAnnotation(Class resourceClass, Collection<ASTAnnotation> annotations) {
        for (ASTAnnotation astAnnotation : annotations) {
            if (astAnnotation.getASTType().getName().equals(resourceClass.getCanonicalName())) {
                return astAnnotation;
            }
        }
        return null;
    }
}
