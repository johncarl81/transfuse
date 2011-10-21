package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

/**
 * @author John Ericksen
 */
public class ASTElementParameter extends ASTElementBase implements ASTParameter {

    private ASTType astType;

    public ASTElementParameter(VariableElement variableElement, ASTType astType) {
        this((Element) variableElement, astType);
    }

    public ASTElementParameter(TypeParameterElement typeParameterElement, ASTType astType) {
        this((Element) typeParameterElement, astType);
    }

    private ASTElementParameter(Element element, ASTType astType) {
        super(element);
        this.astType = astType;
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }
}
