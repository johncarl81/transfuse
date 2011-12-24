package org.androidrobotics.gen.variableBuilder;

import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public class ContextVariableInjectionNodeBuilder implements InjectionNodeBuilder {
    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        InjectionNode injectionNode = new InjectionNode(astType);

        injectionNode.addAspect(VariableBuilder.class, new ContextVariableBuilder());

        return injectionNode;
    }
}
