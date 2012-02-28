package org.androidtransfuse.analysis.adapter;

import javax.lang.model.element.Element;

/**
 * Adapter class to load the element type on demand.
 *
 * @author John Ericksen
 */
public class ElementASTTypeLazyLoader extends ASTTypeLazyLoader<Element> {
    public ElementASTTypeLazyLoader(Element element, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        super(element, astTypeBuilderVisitor);
    }

    @Override
    protected ASTType buildASTType(Element element, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        return element.asType().accept(astTypeBuilderVisitor, null);
    }
}
