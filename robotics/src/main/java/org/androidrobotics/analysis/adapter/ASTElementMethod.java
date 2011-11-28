package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

/**
 * Element specific implementation of the AST Method
 *
 * @author John Ericksen
 */
public class ASTElementMethod extends ASTElementBase implements ASTMethod {

    private List<ASTParameter> parameters;
    private ASTType returnType;
    private ExecutableElement executableElement;
    private ASTTypeBuilderVisitor astTypeBuilderVisitor;

    public ASTElementMethod(ExecutableElement executableElement, ASTTypeBuilderVisitor astTypeBuilderVisitor, List<ASTParameter> parameters) {
        super(executableElement);
        this.executableElement = executableElement;
        this.parameters = parameters;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }

    @Override
    public synchronized ASTType getReturnType() {
        if (returnType == null) {
            returnType = executableElement.getReturnType().accept(astTypeBuilderVisitor, null);
        }
        return returnType;
    }
}
