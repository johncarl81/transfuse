package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class TypedExpressionFactory {

    private final ASTClassFactory astClassFactory;

    @Inject
    public TypedExpressionFactory(ASTClassFactory astClassFactory) {
        this.astClassFactory = astClassFactory;
    }

    public TypedExpression build(Class type, JExpression expression) {
        return build(astClassFactory.getType(type), expression);
    }

    public TypedExpression build(ASTType astType, JExpression expression) {
        return new TypedExpression(astType, expression);
    }
}
