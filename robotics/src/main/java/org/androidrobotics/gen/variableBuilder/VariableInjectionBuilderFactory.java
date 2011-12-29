package org.androidrobotics.gen.variableBuilder;

import com.sun.codemodel.JType;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public interface VariableInjectionBuilderFactory {

    ProviderInjectionNodeBuilder buildProviderInjectionNodeBuilder(Class<? extends Provider<?>> providerClass);

    VariableImplementationInjectionNodeBuilder buildVariableInjectionBuilder(Class<?> concreteClass);

    VariableASTImplementationInjectionNodeBuilder buildVariableInjectionNodeBuilder(ASTType astType);

    SystemServiceVariableBuilder buildSystemServiceVariableBuilder(String systemService, JType systemServiceType, InjectionNode contextInjectionNode);

    SystemServiceInjectionNodeBuilder buildSystemServiceInjectionNodeBuilder(String systemService, Class<?> systemServiceClass);
}
