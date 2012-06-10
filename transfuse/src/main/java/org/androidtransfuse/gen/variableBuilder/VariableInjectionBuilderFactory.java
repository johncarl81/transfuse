package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
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

    SystemServiceVariableBuilder buildSystemServiceVariableBuilder(String systemService, InjectionNode contextInjectionNode);

    SystemServiceInjectionNodeBuilder buildSystemServiceInjectionNodeBuilder(String systemService, ASTType systemServiceClass);

    ResourceVariableBuilder buildResourceVariableBuilder(int resourceId, ResourceExpressionBuilder resourceExpressionBuilder);

    ExtraValuableBuilder buildExtraVariableBuilder(String extraId, InjectionNode activityInjectionNode, @Assisted("nullable") boolean nullable, @Assisted("wrapped") boolean wrapped);

    ApplicationVariableBuilder buildApplicationVariableBuilder(InjectionNode contextInjectionNode);

    ResourcesVariableBuilder buildResourcesVariableBuilder(InjectionNode applicationInjectionNode);

    ProviderVariableBuilder buildProviderVariableBuilder(InjectionNode providerInjectionNode);

    ViewVariableBuilder buildViewVariableBuilder(Integer viewId, InjectionNode activityInjectionNode, JType jType);

    GeneratedProviderVariableBuilder buildGeneratedProviderVariableBuilder(InjectionNode providerTypeInjectionNode);

    ContextVariableBuilder buildContextVariableBuilder(Class clazz);

    ContextVariableInjectionNodeBuilder buildContextVariableInjectionNodeBuilder(Class clazz);

    PreferenceVariableBuilder buildPreferenceVariableBuilder(ASTType preferenceType, String preferenceName, InjectionNode preferenceManagerInjectionNode);

    SharedPreferenceManagerInjectionNodeBuilder buildSharedPreferenceManagerInjectionNodeBuilder();

    SharedPreferenceManagerVariableBuilder buildSharedPreferenceManagerVariableBuilder(InjectionNode contextInjectionNode);
}
