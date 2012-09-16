package org.androidtransfuse.analysis.adapter;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class specific AST Parameter
 *
 * @author John Ericksen
 */
public class ASTClassParameter implements ASTParameter {

    private final ASTType astType;
    private final Map<Class<?>, Annotation> annotationMap;
    private final Collection<ASTAnnotation> annotations;

    public ASTClassParameter(Annotation[] annotations, ASTType astType, Collection<ASTAnnotation> astAnnotations) {
        this.annotations = astAnnotations;
        this.astType = astType;

        Map<Class<?>, Annotation> classAnnotationMap = new HashMap<Class<?>, Annotation>();
        for (Annotation annotation : annotations) {
            classAnnotationMap.put(annotation.getClass(), annotation);
        }

        this.annotationMap = Collections.unmodifiableMap(classAnnotationMap);
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

    @Override
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }
}
