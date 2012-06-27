package org.androidtransfuse.analysis.adapter;

import java.util.List;

/**
 * Abstract Syntax Tree Constructor node
 *
 * @author John Ericksen
 */
public interface ASTConstructor extends ASTBase {

    /**
     * Supplies the contained constructor parameters
     *
     * @return list of constructor parameters
     */
    List<ASTParameter> getParameters();

    /**
     * Access modifier for the constructor
     *
     * @return
     */
    ASTAccessModifier getAccessModifier();

    /**
     * Supplies all throws associated with this method
     *
     * @return throw types
     */
    List<ASTType> getThrowsTypes();
}
