package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.model.TypedExpression;

/**
 * @author John Ericksen
 */
public class ActivityTypeDelegateASTReference implements ActivityDelegateASTReference{

    @Override
    public JExpression buildReference(TypedExpression rootExpression) {
        return rootExpression.getExpression();
    }
}
