package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.Element;

/**
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
