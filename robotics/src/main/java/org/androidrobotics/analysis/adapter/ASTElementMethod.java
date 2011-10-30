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

    public ASTElementMethod(ExecutableElement executableElement, ASTType returnType, List<ASTParameter> parameters) {
        super(executableElement);
        this.parameters = parameters;
        this.returnType = returnType;
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }

    @Override
    public ASTType getReturnType() {
        return returnType;
    }
}
