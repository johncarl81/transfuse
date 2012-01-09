package org.androidrobotics.analysis.adapter;

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

    ASTAccessModifier getAccessModifier();
}
