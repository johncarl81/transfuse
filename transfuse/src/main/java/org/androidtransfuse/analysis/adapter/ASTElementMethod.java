package org.androidtransfuse.analysis.adapter;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

/**
 * Element specific implementation of the AST Method
 *
 * @author John Ericksen
 */
public class ASTElementMethod extends ASTElementBase implements ASTMethod {

    private final ASTTypeLazyLoader<ExecutableElement> astTypeLoader;
    private final ImmutableList<ASTParameter> parameters;
    private final ASTAccessModifier modifier;
    private final ImmutableList<ASTType> throwsTypes;

    public ASTElementMethod(ExecutableElement executableElement,
                            ASTTypeBuilderVisitor astTypeBuilderVisitor,
                            ImmutableList<ASTParameter> parameters,
                            ASTAccessModifier modifier,
                            ImmutableCollection<ASTAnnotation> annotations,
                            ImmutableList<ASTType> throwsTypes) {
        super(executableElement, annotations);
        this.modifier = modifier;
        this.throwsTypes = throwsTypes;
        this.astTypeLoader = new ASTMethodTypeLazyLoader(executableElement, astTypeBuilderVisitor);
        this.parameters = parameters;
    }

    private static final class ASTMethodTypeLazyLoader extends ASTTypeLazyLoader<ExecutableElement> {
        public ASTMethodTypeLazyLoader(ExecutableElement element, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
            super(element, astTypeBuilderVisitor);
        }

        @Override
        protected ASTType buildASTType(ExecutableElement element, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
            return element.getReturnType().accept(astTypeBuilderVisitor, null);
        }
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }

    @Override
    public ASTType getReturnType() {
        return astTypeLoader.getASTType();
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }

    @Override
    public List<ASTType> getThrowsTypes() {
        return throwsTypes;
    }
}
