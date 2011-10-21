package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTElementMethod extends ASTElementBase implements ASTMethod {

    private List<ASTParameter> parameters;

    public ASTElementMethod(ExecutableElement executableElement, List<ASTParameter> parameters) {
        super(executableElement);
        this.parameters = parameters;
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }
}
