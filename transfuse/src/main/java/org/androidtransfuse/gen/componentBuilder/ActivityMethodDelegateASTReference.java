package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Collections;

/**
 * @author John Ericksen
 */
public class ActivityMethodDelegateASTReference implements ActivityDelegateASTReference {

    private final ASTMethod method;
    private final InvocationBuilder invocationBuilder;

    @Inject
    public ActivityMethodDelegateASTReference(@Assisted ASTMethod method, InvocationBuilder invocationBuilder) {
        this.method = method;
        this.invocationBuilder = invocationBuilder;
    }

    @Override
    public JExpression buildReference(TypedExpression rootExpression) {
        return invocationBuilder.buildMethodCall(
                method.getAccessModifier(),
                method.getReturnType(),
                method.getName(),
                Collections.EMPTY_LIST,
                Collections.EMPTY_LIST,
                rootExpression.getType(),
                rootExpression.getExpression());
    }
}
