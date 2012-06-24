package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

/**
 * @author John Ericksen
 */
public abstract class ConsistentTypeVariableBuilder implements VariableBuilder {

    private TypedExpressionFactory typedExpressionFactory;
    private Class clazz;
    private ASTType astType;

    public ConsistentTypeVariableBuilder(Class clazz, TypedExpressionFactory typedExpressionFactory) {
        this.clazz = clazz;
        this.astType = null;
        this.typedExpressionFactory = typedExpressionFactory;
    }

    public ConsistentTypeVariableBuilder(ASTType astType, TypedExpressionFactory typedExpressionFactory) {
        this.clazz = null;
        this.astType = astType;
        this.typedExpressionFactory = typedExpressionFactory;
    }

    @Override
    public TypedExpression buildVariable(InjectionBuilderContext context, InjectionNode injectionNode) {
        if(clazz != null){
            return typedExpressionFactory.build(clazz, buildExpression(context, injectionNode));
        }
        else{
            return typedExpressionFactory.build(astType, buildExpression(context, injectionNode));
        }
    }

    public abstract JExpression buildExpression(InjectionBuilderContext context, InjectionNode injectionNode);
}
