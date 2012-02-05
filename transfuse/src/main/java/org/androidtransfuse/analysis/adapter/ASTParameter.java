package org.androidtransfuse.analysis.adapter;

/**
 * Abstract Syntax Tree parameter.  Referenced by the Constructor and Method AST nodes
 *
 * @author John Ericksen
 */
public interface ASTParameter extends ASTBase {

    /**
     * Supplies the type of this parameter
     *
     * @return parameter type
     */
    ASTType getASTType();
}
