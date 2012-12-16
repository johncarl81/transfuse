package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ProvidesVariableBuilder extends ConsistentTypeVariableBuilder {

    private final InjectionNode module;
    private final ASTMethod method;
    private final Map<ASTParameter, InjectionNode> dependencyAnalysis;
    private final InjectionExpressionBuilder injectionExpressionBuilder;
    private final InvocationBuilder invocationBuilder;

    @Inject
    public ProvidesVariableBuilder(@Assisted InjectionNode module,
                                   @Assisted ASTMethod method,
                                   @Assisted Map<ASTParameter, InjectionNode> dependencyAnalysis,
                                   InjectionExpressionBuilder injectionExpressionBuilder,
                                   TypedExpressionFactory typedExpressionFactory,
                                   InvocationBuilder invocationBuilder) {
        super(method.getReturnType(), typedExpressionFactory);
        this.module = module;
        this.method = method;
        this.dependencyAnalysis = dependencyAnalysis;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.invocationBuilder = invocationBuilder;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext context, InjectionNode injectionNode) {
        JExpression moduleVar = injectionExpressionBuilder.buildVariable(context, module).getExpression();

        List<JExpression> paramExpressions = new ArrayList<JExpression>();
        List<ASTType> paramTypes = new ArrayList<ASTType>();

        for (ASTParameter parameter : method.getParameters()) {
            JExpression paramExpression = injectionExpressionBuilder.buildVariable(context, dependencyAnalysis.get(parameter)).getExpression();
            paramExpressions.add(paramExpression);
            paramTypes.add(parameter.getASTType());
        }

        return invocationBuilder.buildMethodCall(
                method.getAccessModifier(),
                method.getReturnType(),
                method.getName(),
                paramExpressions,
                paramTypes,
                module.getASTType(),
                moduleVar);
    }
}
