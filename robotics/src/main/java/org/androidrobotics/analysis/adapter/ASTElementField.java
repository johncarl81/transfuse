package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.VariableElement;

/**
 * @author John Ericksen
 */
public class ASTElementField extends ASTElementBase implements ASTField {

    private VariableElement variableElement;
    private ASTType astType;

    public ASTElementField(VariableElement variableElement, ASTType astType) {
        super(variableElement);
        this.variableElement = variableElement;
        this.astType = astType;
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }
}
