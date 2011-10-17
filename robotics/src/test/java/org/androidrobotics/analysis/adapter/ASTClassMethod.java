package org.androidrobotics.analysis.adapter;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTClassMethod implements ASTMethod {

    private Method method;
    private List<ASTParameter> parameters;

    public ASTClassMethod(Method method, List<ASTParameter> parameters) {
        this.method = method;
        this.parameters = parameters;
    }

    @Override
    public boolean isAnnotated(Class<Inject> annotation) {
        return method.isAnnotationPresent(annotation);
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }
}
