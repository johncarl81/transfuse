package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class StaticInvocationVariableBuilder implements DependentVariableBuilder{

    private Class invocationTarget;
    private String staticInvocation;
    private JCodeModel codeModel;

    @Inject
    public StaticInvocationVariableBuilder(@Assisted Class invocationTarget,
                                           @Assisted String staticInvocation,
                                           JCodeModel codeModel) {
        this.invocationTarget = invocationTarget;
        this.staticInvocation = staticInvocation;
        this.codeModel = codeModel;
    }

    @Override
    public JExpression buildVariable(JExpression dependencyExpression) {
        return codeModel.ref(invocationTarget).staticInvoke(staticInvocation).arg(dependencyExpression);
    }
}
