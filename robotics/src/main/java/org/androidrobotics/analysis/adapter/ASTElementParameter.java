package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

/**
 * Element specific implementation of a AST method parameter
 *
 * @author John Ericksen
 */
public class ASTElementParameter extends ASTElementBase implements ASTParameter {

    private ASTTypeLazyLoader<Element> astTypeLoader;

    public ASTElementParameter(VariableElement variableElement, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        this((Element) variableElement, astTypeBuilderVisitor);
    }

    public ASTElementParameter(TypeParameterElement typeParameterElement, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        this((Element) typeParameterElement, astTypeBuilderVisitor);
    }

    private ASTElementParameter(Element element, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        super(element);
        this.astTypeLoader = new ElementASTTypeLazyLoader(element, astTypeBuilderVisitor);
    }

    @Override
    public synchronized ASTType getASTType() {
        return astTypeLoader.getASTType();
    }
}
