package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ASTClassParameter implements ASTParameter {

    private TypeVariable typeVariable;
    private ASTType astType;
    private Map<Class<?>, Annotation> annotationMap = new HashMap<Class<?>, Annotation>();

    public ASTClassParameter(TypeVariable typeVariable, Annotation[] annotations, ASTType astType) {
        this.typeVariable = typeVariable;
        this.astType = astType;

        for (Annotation annotation : annotations) {
            annotationMap.put(annotation.getClass(), annotation);
        }
    }

    @Override
    public String getName() {
        return typeVariable.getName();
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return (A) annotationMap.get(annotation);
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }
}
