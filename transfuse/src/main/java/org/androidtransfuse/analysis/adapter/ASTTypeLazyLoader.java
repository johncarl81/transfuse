package org.androidtransfuse.analysis.adapter;

import javax.lang.model.element.Element;

/**
 * AST Type loader, lazily loading the getASTType() return value from the abstract buildASTType() method
 *
 * @author John Ericksen
 */
public abstract class ASTTypeLazyLoader<T extends Element> {

    private ASTType astType = null;
    private T element;
    private ASTTypeBuilderVisitor astTypeBuilderVisitor;

    public ASTTypeLazyLoader(T element, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        this.element = element;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
    }

    public synchronized ASTType getASTType() {
        if (astType == null) {
            astType = buildASTType(element, astTypeBuilderVisitor);
        }
        return astType;
    }

    protected abstract ASTType buildASTType(T element, ASTTypeBuilderVisitor astTypeBuilderVisitor);
}
