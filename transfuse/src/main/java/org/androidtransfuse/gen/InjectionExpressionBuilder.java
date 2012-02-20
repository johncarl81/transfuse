package org.androidtransfuse.gen;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface InjectionExpressionBuilder {

    JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode);

    void setupInjectionRequirements(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode);
}
