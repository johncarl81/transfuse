package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.TypedExpression;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public abstract class ConsistentTypeVariableBuilder implements VariableBuilder {

    private TypedExpressionFactory typedExpressionFactory;
    private Class clazz;
    private ASTType astType;

    public ConsistentTypeVariableBuilder(Class clazz) {
        this.clazz = clazz;
    }

    public ConsistentTypeVariableBuilder(ASTType astType) {
        this.astType = astType;
    }

    @Inject
    public void setTypedExpressionFactory(TypedExpressionFactory typedExpressionFactory) {
        this.typedExpressionFactory = typedExpressionFactory;
    }

    @Override
    public TypedExpression buildVariable(InjectionBuilderContext context, InjectionNode injectionNode) {
        if (clazz != null) {
            return typedExpressionFactory.build(clazz, buildExpression(context, injectionNode));
        }
        if (astType != null) {
            return typedExpressionFactory.build(astType, buildExpression(context, injectionNode));
        }
        throw new TransfuseAnalysisException("Error building TypedVariable");
    }

    public abstract JExpression buildExpression(InjectionBuilderContext context, InjectionNode injectionNode);
}
