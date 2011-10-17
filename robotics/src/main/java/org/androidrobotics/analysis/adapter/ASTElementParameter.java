package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

/**
 * @author John Ericksen
 */
public class ASTElementParameter implements ASTParameter {

    private String name;
    private ASTType astType;

    public ASTElementParameter(VariableElement variableElement, ASTType astType) {
        this(variableElement.getSimpleName().toString(), astType);
    }

    public ASTElementParameter(TypeParameterElement typeParameterElement, ASTType astType) {
        this(typeParameterElement.getSimpleName().toString(), astType);
    }

    public ASTElementParameter(String name, ASTType astType) {
        this.name = name;
        this.astType = astType;
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }

    @Override
    public String getName() {
        return name;
    }
}
