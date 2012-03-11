package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.gen.UniqueVariableNamer;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ReturningMethodGenerator extends SimpleMethodGenerator {

    private ASTMethod overrideMethod;
    private boolean superCall;
    private JCodeModel codeModel;
    private JExpression expression;

    @Inject
    public ReturningMethodGenerator(@Assisted ASTMethod overrideMethod, @Assisted boolean superCall, @Assisted JExpression expression, JCodeModel codeModel, UniqueVariableNamer variableNamer) {
        super(overrideMethod, superCall, codeModel, variableNamer);
        this.expression = expression;
    }

    @Override
    public void closeMethod(MethodDescriptor methodDescriptor) {
        if (methodDescriptor != null) {
            methodDescriptor.getMethod().body()._return(expression);
        }
    }
}
