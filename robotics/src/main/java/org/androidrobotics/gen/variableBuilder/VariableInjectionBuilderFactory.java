package org.androidrobotics.gen.variableBuilder;

import com.sun.codemodel.JType;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.variableBuilder.resource.ResourceExpressionBuilder;
import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface VariableInjectionBuilderFactory {

    ProviderInjectionNodeBuilder buildProviderInjectionNodeBuilder(ASTType astType);

    VariableASTImplementationInjectionNodeBuilder buildVariableInjectionNodeBuilder(ASTType astType);

    SystemServiceVariableBuilder buildSystemServiceVariableBuilder(String systemService, JType systemServiceType, InjectionNode contextInjectionNode);

    SystemServiceInjectionNodeBuilder buildSystemServiceInjectionNodeBuilder(String systemService, Class<?> systemServiceClass);

    ResourceVariableBuilder buildResourceVariableBuilder(int resourceId, ResourceExpressionBuilder resourceExpressionBuilder);
}
