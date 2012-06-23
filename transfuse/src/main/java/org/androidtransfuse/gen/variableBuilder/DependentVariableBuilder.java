package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.JExpression;

/**
 * @author John Ericksen
 */
public interface DependentVariableBuilder {

    JExpression buildVariable(JExpression dependencyExpression);
}
