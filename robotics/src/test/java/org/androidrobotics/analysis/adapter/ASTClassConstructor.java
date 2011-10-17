package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTClassConstructor implements ASTConstructor {

    private Constructor constructor;
    private List<ASTParameter> parameters;

    public ASTClassConstructor(Constructor<?> constructor, List<ASTParameter> parameters) {
        this.constructor = constructor;
        this.parameters = parameters;
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return constructor.isAnnotationPresent(annotation);
    }

    @Override
    public List<ASTParameter> getParameters() {
        return parameters;
    }

    @Override
    public String getName() {
        return constructor.getName();
    }
}
