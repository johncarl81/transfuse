package org.androidtransfuse.model;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class TypedExpression {

    private JExpression expression;
    private ASTType type;

    public TypedExpression(ASTType type, JExpression expression) {
        this.expression = expression;
        this.type = type;
    }

    public JExpression getExpression() {
        return expression;
    }

    public ASTType getType() {
        return type;
    }
}
