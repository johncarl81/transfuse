package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Collections;

/**
 * @author John Ericksen
 */
public class ActivityMethodDelegateASTReference implements ActivityDelegateASTReference {

    private ASTMethod method;
    private InvocationBuilder invocationBuilder;

    @Inject
    public ActivityMethodDelegateASTReference(@Assisted ASTMethod method, InvocationBuilder invocationBuilder) {
        this.method = method;
        this.invocationBuilder = invocationBuilder;
    }

    @Override
    public JExpression buildReference(TypedExpression rootExpression) {
        try {
            return invocationBuilder.buildMethodCall(method.getReturnType().getName(),
                    Collections.EMPTY_LIST,
                    Collections.EMPTY_MAP,
                    rootExpression.getType(),
                    rootExpression.getExpression(),
                    method);
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Unable to find class", e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Class already exists", e);
        }
    }
}
