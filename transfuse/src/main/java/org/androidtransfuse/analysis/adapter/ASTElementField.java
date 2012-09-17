package org.androidtransfuse.analysis.adapter;

import com.google.common.collect.ImmutableCollection;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

/**
 * Element specific implementation of the AST Field
 *
 * @author John Ericksen
 */
public class ASTElementField extends ASTElementBase implements ASTField {

    private final ASTTypeLazyLoader<Element> astTypeLoader;
    private final ASTAccessModifier modifier;
    private final VariableElement variableElement;

    public ASTElementField(VariableElement variableElement,
                           ASTTypeBuilderVisitor astTypeBuilderVisitor,
                           ASTAccessModifier modifier,
                           ImmutableCollection<ASTAnnotation> annotations) {
        super(variableElement, annotations);
        this.variableElement = variableElement;
        this.modifier = modifier;
        this.astTypeLoader = new ElementASTTypeLazyLoader(variableElement, astTypeBuilderVisitor);
    }

    @Override
    public synchronized ASTType getASTType() {
        return astTypeLoader.getASTType();
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }

    @Override
    public Object getConstantValue() {
        return variableElement.getConstantValue();
    }
}
