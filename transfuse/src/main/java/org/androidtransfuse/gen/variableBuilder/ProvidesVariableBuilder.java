package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ProvidesVariableBuilder extends ConsistentTypeVariableBuilder {

    private final InjectionNode module;
    private final ASTMethod method;
    private final Map<ASTParameter, InjectionNode> dependencyAnalysis;
    private final InjectionExpressionBuilder injectionExpressionBuilder;

    @Inject
    public ProvidesVariableBuilder(@Assisted InjectionNode module,
                                   @Assisted ASTMethod method,
                                   @Assisted Map<ASTParameter, InjectionNode> dependencyAnalysis,
                                   InjectionExpressionBuilder injectionExpressionBuilder,
                                   TypedExpressionFactory typedExpressionFactory) {
        super(method.getReturnType(), typedExpressionFactory);
        this.module = module;
        this.method = method;
        this.dependencyAnalysis = dependencyAnalysis;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext context, InjectionNode injectionNode) {
        JExpression moduleVar = injectionExpressionBuilder.buildVariable(context, module).getExpression();

        JInvocation methodInvoke = moduleVar.invoke(method.getName());

        for (ASTParameter parameter : method.getParameters()) {
            JExpression paramExpression = injectionExpressionBuilder.buildVariable(context, dependencyAnalysis.get(parameter)).getExpression();
            methodInvoke.arg(paramExpression);
        }

        return methodInvoke;
    }
}
