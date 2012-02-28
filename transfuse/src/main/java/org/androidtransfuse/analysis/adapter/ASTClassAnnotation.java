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
            Method typeParameters = annotation.annotationType().getMethod(value);

            Class convertedType = type;

            if (type.equals(ASTType.class)) {
                convertedType = Class.class;
            }

            if (!typeParameters.getReturnType().isAssignableFrom(convertedType)) {
                throw new TransfuseAnalysisException("Type not expected: " + convertedType);
            }

            return (T) typeParameters.invoke(annotation);

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
