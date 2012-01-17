package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

/**
 * Element specific implementation of the AST Field
 *
 * @author John Ericksen
 */
public class ASTElementField extends ASTElementBase implements ASTField {

    private ASTTypeLazyLoader<Element> astTypeLoader;
    private ASTAccessModifier modifier;
    private VariableElement variableElement;

    public ASTElementField(VariableElement variableElement, ASTTypeBuilderVisitor astTypeBuilderVisitor, ASTAccessModifier modifier) {
        super(variableElement);
        this.variableElement = variableElement;
        this.modifier = modifier;
        this.astTypeLoader = new ElementASTTypeLazyLoader(variableElement, astTypeBuilderVisitor);
    }

    @Override
    public synchronized ASTType getASTType() {
        return astTypeLoader.getASTType();
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }

    @Override
    public Object getConstantValue() {
        return variableElement.getConstantValue();
    }
}
