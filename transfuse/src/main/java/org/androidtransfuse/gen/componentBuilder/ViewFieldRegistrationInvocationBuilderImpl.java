package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ViewFieldRegistrationInvocationBuilderImpl implements ViewRegistrationInvocationBuilder{

    private final InvocationBuilder invocationBuilder;
    private final ASTField astField;
    private final JCodeModel codeModel;

    @Inject
    public ViewFieldRegistrationInvocationBuilderImpl(@Assisted ASTField astField, InvocationBuilder invocationBuilder, JCodeModel codeModel) {
        this.invocationBuilder = invocationBuilder;
        this.astField = astField;
        this.codeModel = codeModel;
    }

    @Override
    public void buildInvocation(JBlock block, TypedExpression expression, JExpression viewExpression, String method, InjectionNode injectionNode) {
        block.invoke(viewExpression, method)
                .arg(invocationBuilder.buildFieldGet(astField.getASTType(),
                        expression.getType(),
                        expression.getExpression(),
                        astField.getName(),
                        astField.getAccessModifier()));
    }
}
