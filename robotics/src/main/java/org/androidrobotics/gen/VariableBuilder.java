package org.androidrobotics.gen;

import com.sun.codemodel.JExpression;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface VariableBuilder {

    JExpression buildVariable(InjectionBuilderContext injectionBuilderContext);

    InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context);
}
