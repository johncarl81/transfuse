package org.androidrobotics.analysis.adapter;

/**
 * Abstract Syntax Tree Field node
 *
 * @author John Ericksen
 */
public interface ASTField extends ASTBase {

    /**
     * Supplies the type of the field represented.
     *
     * @return field type
     */
    ASTType getASTType();

    ASTAccessModifier getAccessModifier();

    Object getConstantValue();
}
