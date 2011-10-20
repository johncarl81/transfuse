package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

/**
 * Element specific implementation of the AST Constructor
 *
 * @author John Ericksen
 */
public class ASTElementConstructor extends ASTElementBase implements ASTConstructor {

    private List<ASTParameter> parameters;

    public ASTElementConstructor(ExecutableElement executableElement, List<ASTParameter> parameters) {
        super(executableElement);
        this.parameters = parameters;
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }
}
