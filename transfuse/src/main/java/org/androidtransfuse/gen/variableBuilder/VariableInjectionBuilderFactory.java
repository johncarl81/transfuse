package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.JType;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.variableBuilder.resource.ResourceExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface VariableInjectionBuilderFactory {

    ProviderInjectionNodeBuilder buildProviderInjectionNodeBuilder(ASTType astType);

    VariableASTImplementationInjectionNodeBuilder buildVariableInjectionNodeBuilder(ASTType astType);

    SystemServiceVariableBuilder buildSystemServiceVariableBuilder(String systemService, JType systemServiceType, InjectionNode contextInjectionNode);

    SystemServiceInjectionNodeBuilder buildSystemServiceInjectionNodeBuilder(String systemService, Class<?> systemServiceClass);

    ResourceVariableBuilder buildResourceVariableBuilder(int resourceId, ResourceExpressionBuilder resourceExpressionBuilder);

    ExtraValuableBuilder buildExtraVariableBuilder(String extraId, InjectionNode activityInjectionNode, JType type, boolean nullable);

    ApplicationVariableBuilder buildApplicationVariableBuilder(InjectionNode contextInjectionNode);

    ResourcesVariableBuilder buildResourcesVariableBuilder(InjectionNode applicationInjectionNode);

    ProviderVariableBuilder buildProviderVariableBuilder(InjectionNode providerInjectionNode);

    ViewVariableBuilder buildViewVariableBuilder(Integer viewId, InjectionNode activityInjectionNode, JType jType);

    GeneratedProviderVariableBuilder buildGeneratedProviderVariableBuilder(InjectionNode providerTypeInjectionNode);
}
