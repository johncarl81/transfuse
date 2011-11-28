package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.VariableElement;

/**
 * Element specific implementation of the AST Field
 *
 * @author John Ericksen
 */
public class ASTElementField extends ASTElementBase implements ASTField {

    private ASTType astType;
    private VariableElement variableElement;
    private ASTTypeBuilderVisitor astTypeBuilderVisitor;

    public ASTElementField(VariableElement variableElement, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        super(variableElement);
        this.variableElement = variableElement;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
    }

    @Override
    public synchronized ASTType getASTType() {
        if (astType == null) {
            astType = variableElement.asType().accept(astTypeBuilderVisitor, null);
        }
        return astType;
    }
}
