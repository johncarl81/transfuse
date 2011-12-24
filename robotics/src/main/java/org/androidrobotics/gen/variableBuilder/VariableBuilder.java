package org.androidrobotics.gen.variableBuilder;

import com.sun.codemodel.JExpression;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface VariableBuilder {

    JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode);
}
