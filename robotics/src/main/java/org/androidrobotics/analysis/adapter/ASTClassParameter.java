package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ASTClassParameter implements ASTParameter {

    private ASTType astType;
    private Map<Class<?>, Annotation> annotationMap = new HashMap<Class<?>, Annotation>();

    public ASTClassParameter(Annotation[] annotations, ASTType astType) {
        this.astType = astType;

        for (Annotation annotation : annotations) {
            annotationMap.put(annotation.getClass(), annotation);
        }
    }

    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return annotationMap.containsKey(annotation);
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return (A) annotationMap.get(annotation);
    }

    @Override
    public String getName() {
        return astType.getName();
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }

    @Override
    public List<ASTAnnotation> getAnnotations() {
        List<ASTAnnotation> annotationList = new ArrayList<ASTAnnotation>();

        for (Annotation annotation : annotationMap.values()) {
            annotationList.add(new ASTClassAnnotation(annotation));
        }

        return annotationList;
    }
}
