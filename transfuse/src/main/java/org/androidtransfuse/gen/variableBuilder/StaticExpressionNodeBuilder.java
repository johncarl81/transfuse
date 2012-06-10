package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

/**
 * @author John Ericksen
 */
public class StaticExpressionNodeBuilder extends InjectionNodeBuilderNoAnnotationAdapter {

    private TypedExpression expression;

    public StaticExpressionNodeBuilder(TypedExpression expression) {
        this.expression = expression;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        InjectionNode injectionNode = new InjectionNode(astType);

        injectionNode.addAspect(VariableBuilder.class, new VariableBuilder() {
            @Override
            public TypedExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
                return expression;
            }
        });

        return injectionNode;
    }
}
