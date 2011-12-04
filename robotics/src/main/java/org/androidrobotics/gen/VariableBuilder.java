package org.androidrobotics.gen;

import com.sun.codemodel.JExpression;
import org.androidrobotics.analysis.AnalysisDependencyProcessingCallback;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface VariableBuilder {

    JExpression buildVariable(InjectionBuilderContext injectionBuilderContext);

    InjectionNode processInjectionNode(ASTType astType, AnalysisDependencyProcessingCallback callback);
}
