package org.androidtransfuse.analysis.adapter;

import com.google.common.collect.ImmutableCollection;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

/**
 * Element specific implementation of a AST method parameter
 *
 * @author John Ericksen
 */
public class ASTElementParameter extends ASTElementBase implements ASTParameter {

    private final ASTTypeLazyLoader<Element> astTypeLoader;

    public ASTElementParameter(VariableElement variableElement,
                               ASTTypeBuilderVisitor astTypeBuilderVisitor,
                               ImmutableCollection<ASTAnnotation> annotations) {
        this((Element) variableElement, astTypeBuilderVisitor, annotations);
    }

    public ASTElementParameter(TypeParameterElement typeParameterElement,
                               ASTTypeBuilderVisitor astTypeBuilderVisitor,
                               ImmutableCollection<ASTAnnotation> annotations) {
        this((Element) typeParameterElement, astTypeBuilderVisitor, annotations);
    }

    private ASTElementParameter(Element element,
                                ASTTypeBuilderVisitor astTypeBuilderVisitor,
                                ImmutableCollection<ASTAnnotation> annotations) {
        super(element, annotations);
        this.astTypeLoader = new ElementASTTypeLazyLoader(element, astTypeBuilderVisitor);
    }

    @Override
    public synchronized ASTType getASTType() {
        return astTypeLoader.getASTType();
    }
}
