package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.VariableElement;

/**
 * Element specific implementation of the AST Field
 *
 * @author John Ericksen
 */
public class ASTElementField extends ASTElementBase implements ASTField {

    private ASTType astType;

    public ASTElementField(VariableElement variableElement, ASTType astType) {
        super(variableElement);
        this.astType = astType;
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }
}
