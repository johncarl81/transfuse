package org.androidrobotics.analysis.adapter;

import org.androidrobotics.analysis.RoboticsAnalysisException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
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

            if (typeParameters != null) {

                Class convertedType = type;

                if (type.equals(ASTType.class)) {
                    convertedType = Class.class;
                }

                if (typeParameters.getReturnType().isAssignableFrom(convertedType)) {
                    throw new RoboticsAnalysisException("Type not expected: " + convertedType);
                }


                return (T) typeParameters.invoke(annotation);
            } else {
                throw new RoboticsAnalysisException("Annotation method not present: " + value);
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getName() {
        return annotation.annotationType().getName();
    }
}
