package org.androidtransfuse.analysis.adapter;

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

    /**
     * Supplies the access modifier for this field
     *
     * @return field access modifier
     */
    ASTAccessModifier getAccessModifier();

    /**
     * Supplies the constant value for this field (if one exists)
     * <p/>
     * IE: static String value = "constant"
     *
     * @return constant field values
     */
    Object getConstantValue();
}
