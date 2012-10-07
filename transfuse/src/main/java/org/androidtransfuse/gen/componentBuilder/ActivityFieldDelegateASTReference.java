package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ActivityFieldDelegateASTReference implements ActivityDelegateASTReference{

    private final ASTField astField;
    private final InvocationBuilder invocationBuilder;

    @Inject
    public ActivityFieldDelegateASTReference(@Assisted ASTField astField, InvocationBuilder invocationBuilder) {
        this.astField = astField;
        this.invocationBuilder = invocationBuilder;
    }

    @Override
    public JExpression buildReference(TypedExpression rootExpression) {

        return invocationBuilder.buildFieldGet(astField.getASTType(),
                rootExpression.getType(),
                rootExpression.getExpression(),
                astField.getName(),
                astField.getAccessModifier());
    }
}
