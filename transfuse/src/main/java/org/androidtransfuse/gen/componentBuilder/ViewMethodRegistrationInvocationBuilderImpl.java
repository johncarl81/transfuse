package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Collections;

/**
 * @author John Ericksen
 */
public class ViewMethodRegistrationInvocationBuilderImpl implements ViewRegistrationInvocationBuilder {

    private final InvocationBuilder invocationBuilder;
    private final ASTMethod getterMethod;

    @Inject
    public ViewMethodRegistrationInvocationBuilderImpl(@Assisted ASTMethod getterMethod, InvocationBuilder invocationBuilder) {
        this.invocationBuilder = invocationBuilder;
        this.getterMethod = getterMethod;
    }

    @Override
    public void buildInvocation(JBlock block, TypedExpression expression, JExpression viewExpression, String method, InjectionNode injectionNode) {

        try {
            block.invoke(viewExpression, method)
                    .arg(invocationBuilder.buildMethodCall(
                            getterMethod.getReturnType(),
                            Collections.EMPTY_LIST,
                            Collections.EMPTY_MAP,
                            expression.getType(),
                            expression.getExpression(),
                            getterMethod));
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Class not found", e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Class already exists", e);
        }
    }
}
