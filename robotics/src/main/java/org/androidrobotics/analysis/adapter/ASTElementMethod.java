package org.androidrobotics.analysis.adapter;

import javax.inject.Inject;
import javax.lang.model.element.ExecutableElement;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTElementMethod implements ASTMethod {

    private ExecutableElement executableElement;
    private List<ASTParameter> parameters;

    public ASTElementMethod(ExecutableElement executableElement, List<ASTParameter> parameters) {
        this.executableElement = executableElement;
        this.parameters = parameters;
    }

    @Override
    public boolean isAnnotated(Class<Inject> annotation) {
        return executableElement.getAnnotation(annotation) != null;
    }

    @Override
    public String getName() {
        return executableElement.getSimpleName().toString();
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }
}
