package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import java.util.Collection;

/**
 * @author John Ericksen
 */
public abstract class InjectionNodeBuilderNoAnnotationAdapter implements InjectionNodeBuilder {
    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, Collection<ASTAnnotation> annotations) {
        return buildInjectionNode(astType, context);
    }

    public abstract InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context);
}
