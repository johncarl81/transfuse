package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTClassMethod implements ASTMethod {

    private Method method;
    private List<ASTParameter> parameters;
    private ASTType returnType;

    public ASTClassMethod(Method method, ASTType returnType, List<ASTParameter> parameters) {
        this.method = method;
        this.parameters = parameters;
        this.returnType = returnType;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return method.getAnnotation(annotation);
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
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

    @Override
    public ASTType getReturnType() {
        return returnType;
    }
}
