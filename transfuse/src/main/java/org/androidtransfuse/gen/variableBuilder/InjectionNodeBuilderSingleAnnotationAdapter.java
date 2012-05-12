package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public abstract class InjectionNodeBuilderSingleAnnotationAdapter<T extends Annotation> implements InjectionNodeBuilder {

    private Class<T> annotationClass;

    public InjectionNodeBuilderSingleAnnotationAdapter(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, Collection<ASTAnnotation> annotations) {

        ASTAnnotation annotation = getAnnotation(annotationClass, annotations);

        return buildInjectionNode(astType, context, annotation);
    }

    public abstract InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation);

    private ASTAnnotation getAnnotation(Class<? extends Annotation> resourceClass, Collection<ASTAnnotation> annotations) {
        ASTAnnotation annotation = null;

        for (ASTAnnotation astAnnotation : annotations) {
            if (astAnnotation.getName().equals(resourceClass.getName())) {
                annotation = astAnnotation;
            }
        }

        if (annotation == null) {
            throw new TransfuseAnalysisException("Unable to find annotation: " + resourceClass.getName());
        }

        return annotation;
    }
}
