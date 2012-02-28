package org.androidtransfuse.analysis.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Class specific AST Method
 *
 * @author John Ericksen
 */
public class ASTClassMethod implements ASTMethod {

    private Method method;
    private List<ASTParameter> parameters;
    private ASTType returnType;
    private ASTAccessModifier modifier;
    private Collection<ASTAnnotation> annotations;

    public ASTClassMethod(Method method, ASTType returnType, List<ASTParameter> parameters, ASTAccessModifier modifier, Collection<ASTAnnotation> annotations) {
        this.method = method;
        this.parameters = parameters;
        this.returnType = returnType;
        this.modifier = modifier;
        this.annotations = annotations;
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

    @Override
    public Collection<ASTAnnotation> getAnnotations() {
        return annotations;
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }
}
