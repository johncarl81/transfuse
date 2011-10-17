package org.androidrobotics.analysis.adapter;

import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTElementConstructor implements ASTConstructor {

    private ExecutableElement executableElement;
    private List<ASTParameter> parameters;

    public ASTElementConstructor(ExecutableElement executableElement, List<ASTParameter> parameters) {
        this.executableElement = executableElement;
        this.parameters = parameters;
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return executableElement.getAnnotation(annotation) != null;
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }

    @Override
    public String getName() {
        return executableElement.getSimpleName().toString();
    }
}
