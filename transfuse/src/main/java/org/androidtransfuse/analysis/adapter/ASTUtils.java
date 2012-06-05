package org.androidtransfuse.analysis.adapter;

import org.androidtransfuse.analysis.TransfuseAnalysisException;

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
     * @param implement
     * @param extend
     * @return
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
        ASTAnnotation annotation = null;

        for (ASTAnnotation astAnnotation : annotations) {
            if (astAnnotation.getName().equals(resourceClass.getName())) {
                annotation = astAnnotation;
            }
        }

        if (annotation == null) {
            throw new TransfuseAnalysisException("Unable to find annotation: " + resourceClass.getName());
        }

        return annotation;
    }
}
