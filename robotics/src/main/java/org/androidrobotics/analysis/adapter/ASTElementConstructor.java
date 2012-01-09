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
    private ASTAccessModifier modifier;

    public ASTElementConstructor(ExecutableElement executableElement, List<ASTParameter> parameters, ASTAccessModifier modifier) {
        super(executableElement);
        this.parameters = parameters;
        this.modifier = modifier;
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }
}
