package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface VariableBuilder {

    JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode);
}
