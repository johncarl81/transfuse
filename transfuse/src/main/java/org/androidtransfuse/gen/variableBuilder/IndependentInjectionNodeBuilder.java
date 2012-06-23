package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class IndependentInjectionNodeBuilder implements InjectionNodeBuilder {

    private VariableBuilder variableBuilder;

    @Inject
    public IndependentInjectionNodeBuilder(@Assisted VariableBuilder variableBuilder) {
        this.variableBuilder = variableBuilder;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, Collection<ASTAnnotation> annotations) {
        InjectionNode injectionNode = new InjectionNode(astType);

        injectionNode.addAspect(VariableBuilder.class, variableBuilder);

        return injectionNode;
    }
}
