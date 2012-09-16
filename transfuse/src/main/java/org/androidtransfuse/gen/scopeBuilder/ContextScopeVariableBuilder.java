package org.androidtransfuse.gen.scopeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.astAnalyzer.ScopeAspect;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.ProviderGenerator;
import org.androidtransfuse.gen.variableBuilder.TypedExpressionFactory;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.scope.ContextScopeHolder;
import org.androidtransfuse.scope.Scope;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ContextScopeVariableBuilder implements VariableBuilder {

    private final ProviderGenerator providerGenerator;
    private final JCodeModel codeModel;
    private final TypedExpressionFactory typedExpressionFactory;
    private final InjectionNode contextScopeHolder;
    private final InjectionExpressionBuilder injectionExpressionBuilder;

    @Inject
    public ContextScopeVariableBuilder(@Assisted InjectionNode contextScopeHolder,
                                       JCodeModel codeModel,
                                       ProviderGenerator providerGenerator,
                                       TypedExpressionFactory typedExpressionFactory,
                                       InjectionExpressionBuilder injectionExpressionBuilder) {
        this.codeModel = codeModel;
        this.providerGenerator = providerGenerator;
        this.typedExpressionFactory = typedExpressionFactory;
        this.contextScopeHolder = contextScopeHolder;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
    }

    public TypedExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {

        //build provider
        JExpression provider = buildProvider(injectionNode);

        //build scope call
        // <T> T getScopedObject(Class<T> clazz, Provider<T> provider);
        TypedExpression contextScopeHolderExpression = injectionExpressionBuilder.buildVariable(injectionBuilderContext, this.contextScopeHolder);
        JExpression injectionNodeClassRef = codeModel.ref(injectionNode.getClassName()).dotclass();
        //todo:coerce type?
        JExpression cast = JExpr.cast(codeModel.ref(ContextScopeHolder.class), contextScopeHolderExpression.getExpression());
        JExpression scopeVar = cast.invoke(ContextScopeHolder.GET_SCOPE);

        JExpression expression = scopeVar.invoke(Scope.GET_SCOPED_OBJECT).arg(injectionNodeClassRef).arg(provider);

        return typedExpressionFactory.build(injectionNode.getASTType(), expression);
    }

    private JExpression buildProvider(InjectionNode injectionNode) {

        InjectionNode nonScopedInjectionNode = new InjectionNode(injectionNode.getUsageType(), injectionNode.getASTType());

        for (Map.Entry<Class, Object> aspectEntry : injectionNode.getAspects().entrySet()) {
            nonScopedInjectionNode.addAspect(aspectEntry.getKey(), aspectEntry.getValue());
        }

        nonScopedInjectionNode.getAspects().remove(ScopeAspect.class);

        JDefinedClass providerClass = providerGenerator.generateProvider(nonScopedInjectionNode);

        return JExpr._new(providerClass);
    }
}
