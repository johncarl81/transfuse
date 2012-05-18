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
    public <T> T getProperty(String value, Class<T> type) {
        try {
            Method annotationParameter = annotation.annotationType().getMethod(value);

            if (!annotationParameter.getReturnType().isAssignableFrom(type)) {
                throw new TransfuseAnalysisException("Type not expected: " + type);
            }

            return (T) annotationParameter.invoke(annotation);

        } catch (IllegalAccessException e) {
            throw new TransfuseAnalysisException("IllegalAccessException Exception while accessing annotation method: " + value, e);
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("Annotation method not present: " + value, e);
        } catch (InvocationTargetException e) {
            throw new TransfuseAnalysisException("InvocationTargetException Exception while accessing annotation method: " + value, e);
        }
    }

    @Override
    public String getName() {
        return annotation.annotationType().getName();
    }
}
