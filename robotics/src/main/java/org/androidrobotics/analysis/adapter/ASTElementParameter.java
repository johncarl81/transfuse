package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.TypeParameterElement;

/**
 * @author John Ericksen
 */
public class ASTElementParameter implements ASTParameter {

    private TypeParameterElement typeParameterElement;
    private ASTType astType;

    public ASTElementParameter(TypeParameterElement typeParameterElement, ASTType astType) {
        this.typeParameterElement = typeParameterElement;
        this.astType = astType;
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }

    @Override
    public String getName() {
        return typeParameterElement.getSimpleName().toString();
    }
}
