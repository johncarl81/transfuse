package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.model.TypedExpression;

/**
 * @author John Ericksen
 */
public interface ActivityDelegateASTReference {
    JExpression buildReference(TypedExpression rootExpression);
}
