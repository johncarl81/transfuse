package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.adapter.ASTUtils;
import org.androidtransfuse.model.InjectionNode;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public abstract class InjectionNodeBuilderSingleAnnotationAdapter implements InjectionNodeBuilder {

    private final Class<? extends Annotation> annotationClass;

    public InjectionNodeBuilderSingleAnnotationAdapter(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, Collection<ASTAnnotation> annotations) {

        ASTAnnotation annotation = ASTUtils.getInstance().getAnnotation(annotationClass, annotations);

        return buildInjectionNode(astType, context, annotation);
    }

    public abstract InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation);
}
