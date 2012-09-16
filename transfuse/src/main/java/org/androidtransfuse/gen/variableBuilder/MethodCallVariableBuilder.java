package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;

import javax.inject.Inject;
import java.util.List;

/**
 * @author John Ericksen
 */
public class MethodCallVariableBuilder implements DependentVariableBuilder{

    private final String methodName;
    private final List<String> arguments;

    @Inject
    public MethodCallVariableBuilder(@Assisted String methodName,
                                     @Assisted List<String> arguments) {
        this.methodName = methodName;
        this.arguments = arguments;
    }

    @Override
    public JExpression buildVariable(JExpression dependencyExpression) {
        JInvocation invocation = dependencyExpression.invoke(methodName);

        for (String argument : arguments) {
            invocation.arg(argument);
        }

        return invocation;
    }
}
