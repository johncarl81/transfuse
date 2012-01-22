package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ASTClassParameter implements ASTParameter {

    private ASTType astType;
    private Map<Class<?>, Annotation> annotationMap = new HashMap<Class<?>, Annotation>();
    private Collection<ASTAnnotation> annotations;

    public ASTClassParameter(Annotation[] annotations, ASTType astType, Collection<ASTAnnotation> astAnnotations) {
        this.annotations = astAnnotations;
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
    public Collection<ASTAnnotation> getAnnotations() {
        return annotations;
    }
}
