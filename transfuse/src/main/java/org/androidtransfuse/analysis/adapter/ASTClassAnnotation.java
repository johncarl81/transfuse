package org.androidtransfuse.analysis.adapter;

import org.androidtransfuse.analysis.TransfuseAnalysisException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class specific AST Annotation
 *
 * @author John Ericksen
 */
public class ASTClassAnnotation implements ASTAnnotation {

    private Annotation annotation;

    public ASTClassAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    @Override
    public <T> T getProperty(String name, Class<T> type) {
        try {
            Method annotationParameter = annotation.annotationType().getMethod(name);

            if (!annotationParameter.getReturnType().isAssignableFrom(type)) {
                throw new TransfuseAnalysisException("Type not expected: " + type);
            }

            return (T) annotationParameter.invoke(annotation);

        } catch (IllegalAccessException e) {
            throw new TransfuseAnalysisException("IllegalAccessException Exception while accessing annotation method: " + name, e);
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("Annotation method not present: " + name, e);
        } catch (InvocationTargetException e) {
            throw new TransfuseAnalysisException("InvocationTargetException Exception while accessing annotation method: " + name, e);
        }
    }

    @Override
    public String getName() {
        return annotation.annotationType().getName();
    }
}
