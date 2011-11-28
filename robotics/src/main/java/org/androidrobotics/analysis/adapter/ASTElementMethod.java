package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

/**
 * Element specific implementation of the AST Method
 *
 * @author John Ericksen
 */
public class ASTElementMethod extends ASTElementBase implements ASTMethod {

    private ASTTypeLazyLoader<ExecutableElement> astTypeLoader;
    private List<ASTParameter> parameters;

    public ASTElementMethod(ExecutableElement executableElement, ASTTypeBuilderVisitor astTypeBuilderVisitor, List<ASTParameter> parameters) {
        super(executableElement);
        this.astTypeLoader = new ASTTypeLazyLoader<ExecutableElement>(executableElement, astTypeBuilderVisitor) {
            @Override
            protected ASTType buildASTType(ExecutableElement element, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
                return element.getReturnType().accept(astTypeBuilderVisitor, null);
            }
        };
        this.parameters = parameters;
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }

    @Override
    public ASTType getReturnType() {
        return astTypeLoader.getASTType();
    }
}
