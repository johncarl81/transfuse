package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTPrimitiveType;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class TypeInvocationHelper {

    private JCodeModel codeModel;
    private ASTClassFactory astClassFactory;

    @Inject
    public TypeInvocationHelper(JCodeModel codeModel, ASTClassFactory astClassFactory) {
        this.codeModel = codeModel;
        this.astClassFactory = astClassFactory;
    }

    public <T> JExpression getExpression(ASTType type, Map<T, TypedExpression> expressionMap, T parameter) {

        if (parameter == null) {
            return JExpr._null();
        } else if (type != null) {
            return coerceType(type, expressionMap.get(parameter));
        } else {
            return expressionMap.get(parameter).getExpression();
        }
    }

    public JExpression coerceType(ASTType targetType, TypedExpression typedExpression) {
        if (targetType.equals(typedExpression.getType())) {
            return typedExpression.getExpression();
        }
        if (targetType.inheritsFrom(typedExpression.getType())) {
            return JExpr.cast(codeModel.ref(targetType.getName()), typedExpression.getExpression());
        }
        if (targetType instanceof ASTPrimitiveType) {
            ASTPrimitiveType primitiveTargetType = (ASTPrimitiveType) targetType;

            ASTType objectType = astClassFactory.buildASTClassType(primitiveTargetType.getObjectClass());

            if (objectType.inheritsFrom(typedExpression.getType())) {
                return JExpr.cast(codeModel.ref(objectType.getName()), typedExpression.getExpression());
            }
        }
        throw new TransfuseAnalysisException("Non-coercible types encountered");
    }
}
