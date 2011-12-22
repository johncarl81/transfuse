package org.androidrobotics.gen.variableBuilder;

import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface AnnotatedVariableBuilder {

    InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation);
}
