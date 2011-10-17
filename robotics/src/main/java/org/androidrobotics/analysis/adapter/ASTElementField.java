package org.androidrobotics.analysis.adapter;

import javax.inject.Inject;
import javax.lang.model.element.VariableElement;

/**
 * @author John Ericksen
 */
public class ASTElementField implements ASTField {

    private VariableElement variableElement;
    private ASTType astType;

    public ASTElementField(VariableElement variableElement, ASTType astType) {
        this.variableElement = variableElement;
        this.astType = astType;
    }

    @Override
    public boolean isAnnotated(Class<Inject> annotation) {
        return variableElement.getAnnotation(annotation) != null;
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }

    @Override
    public String getName() {
        return variableElement.getSimpleName().toString();
    }
}
