package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import java.util.Collection;

/**
 * Element specific implementation of a AST method parameter
 *
 * @author John Ericksen
 */
public class ASTElementParameter extends ASTElementBase implements ASTParameter {

    private ASTTypeLazyLoader<Element> astTypeLoader;

    public ASTElementParameter(VariableElement variableElement, ASTTypeBuilderVisitor astTypeBuilderVisitor, Collection<ASTAnnotation> annotations) {
        this((Element) variableElement, astTypeBuilderVisitor, annotations);
    }

    public ASTElementParameter(TypeParameterElement typeParameterElement, ASTTypeBuilderVisitor astTypeBuilderVisitor, Collection<ASTAnnotation> annotations) {
        this((Element) typeParameterElement, astTypeBuilderVisitor, annotations);
    }

    private ASTElementParameter(Element element, ASTTypeBuilderVisitor astTypeBuilderVisitor, Collection<ASTAnnotation> annotations) {
        super(element, annotations);
        this.astTypeLoader = new ElementASTTypeLazyLoader(element, astTypeBuilderVisitor);
    }

    @Override
    public synchronized ASTType getASTType() {
        return astTypeLoader.getASTType();
    }
}
