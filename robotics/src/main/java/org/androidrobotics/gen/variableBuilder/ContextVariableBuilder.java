package org.androidrobotics.gen.variableBuilder;

import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public class ContextVariableBuilder implements VariableBuilder {
    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        return JExpr._this();
    }
}
