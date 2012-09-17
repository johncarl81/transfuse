package org.androidtransfuse.gen.variableBuilder;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JType;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.scopeBuilder.ContextScopeVariableBuilder;
import org.androidtransfuse.gen.variableBuilder.resource.ResourceExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

/**
 * @author John Ericksen
 */
public interface VariableInjectionBuilderFactory {

    ProviderInjectionNodeBuilder buildProviderInjectionNodeBuilder(ASTType astType);

    ProviderVariableBuilder buildProviderVariableBuilder(InjectionNode providerInjectionNode);

    VariableASTImplementationInjectionNodeBuilder buildVariableInjectionNodeBuilder(ASTType astType);

    SystemServiceVariableBuilder buildSystemServiceVariableBuilder(String systemService, InjectionNode contextInjectionNode);

    ResourceVariableBuilder buildResourceVariableBuilder(int resourceId, ResourceExpressionBuilder resourceExpressionBuilder);

    ExtraValuableBuilder buildExtraVariableBuilder(String extraId, InjectionNode activityInjectionNode, @Assisted("nullable") boolean nullable, @Assisted("wrapped") boolean wrapped);

    ViewVariableBuilder buildViewVariableBuilder(Integer viewId, String viewTag, InjectionNode activityInjectionNode, JType jType);

    GeneratedProviderVariableBuilder buildGeneratedProviderVariableBuilder(InjectionNode providerTypeInjectionNode);

    PreferenceVariableBuilder buildPreferenceVariableBuilder(ASTType preferenceType, String preferenceName, InjectionNode preferenceManagerInjectionNode);

    StaticInvocationVariableBuilder buildStaticInvocationVariableBuilder(Class invocationTarget, String staticInvocation);

    MethodCallVariableBuilder buildMethodCallVariableBuilder(String methodName, ImmutableList<String> arguments);

    DependentInjectionNodeBuilder buildDependentInjectionNodeBuilder(@Assisted("dependency") Class dependency, @Assisted("returnType") Class returnType, DependentVariableBuilder variableBuilder);

    DependentVariableBuilderWrapper buildDependentVariableBuilderWrapper(InjectionNode dependency, DependentVariableBuilder dependentVariableBuilder, Class type);

    IndependentInjectionNodeBuilder buildInjectionNodeBuilder(VariableBuilder variableBuilder);

    IndependentVariableBuilderWrapper buildIndependentVariableBuilderWrapper(ASTType astType, JExpression expression);

    ExpressionVariableBuilderWrapper buildExpressionWrapper(TypedExpression typedExpression);

    FragmentViewVariableBuilder buildFragmentViewVariableBuilder(Integer viewId, String viewTag, InjectionNode fragmentInjectionNode, JType jType);

    ContextScopeVariableBuilder buildContextScopeVariableBuilder(InjectionNode contextScopeHolder);
}
