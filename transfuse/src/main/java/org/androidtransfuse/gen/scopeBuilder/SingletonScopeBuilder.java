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
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.scope.Scope;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class SingletonScopeBuilder implements VariableBuilder {

    private static final String GET_SCOPED_OBJECT = "getScopedObject";

    private ProviderGenerator providerGenerator;
    private InjectionNode applicationInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;
    private JCodeModel codeModel;

    @Inject
    public SingletonScopeBuilder(@Assisted InjectionNode applicationInjectionNode,
                                 JCodeModel codeModel,
                                 InjectionExpressionBuilder injectionExpressionBuilder,
                                 ProviderGenerator providerGenerator) {
        this.applicationInjectionNode = applicationInjectionNode;
        this.codeModel = codeModel;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.providerGenerator = providerGenerator;
    }

    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {

        JExpression applicationVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, applicationInjectionNode);

        //build provider
        JExpression provider = buildProvider(injectionNode, injectionBuilderContext.getRResource());

        //build scope call
        // <T> T getScopedObject(Class<T> clazz, Provider<T> provider);
        JExpression injectionNodeClassRef = codeModel.ref(injectionNode.getClassName()).dotclass();
        JExpression scopeVar = JExpr.cast(codeModel._ref(Scope.class), applicationVar);

        return scopeVar.invoke(GET_SCOPED_OBJECT).arg(injectionNodeClassRef).arg(provider);
    }

    private JExpression buildProvider(InjectionNode injectionNode, RResource rResource) {

        InjectionNode nonScopedInjectionNode = new InjectionNode(injectionNode.getUsageType(), injectionNode.getASTType());

        for (Map.Entry<Class, Object> aspectEntry : injectionNode.getAspects().entrySet()) {
            nonScopedInjectionNode.addAspect(aspectEntry.getKey(), aspectEntry.getValue());
        }

        nonScopedInjectionNode.getAspects().remove(ScopeAspect.class);

        JDefinedClass providerClass = providerGenerator.generateProvider(nonScopedInjectionNode, rResource);

        return JExpr._new(providerClass);
    }
}
