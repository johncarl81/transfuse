package org.androidtransfuse.gen.componentBuilder;

import android.content.Context;
import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.AnalysisContextFactory;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.gen.variableBuilder.StaticExpressionNodeBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class BroadcastReceiverInjectionNodeFactory implements InjectionNodeFactory {

    private AnalysisContextFactory analysisContextFactory;
    private InjectionPointFactory injectionPointFactory;
    private InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory;
    private InjectionNodeBuilderRepository injectionNodeBuilderRepository;
    private ASTType astType;
    private InjectionBindingBuilder injectionBindingBuilder;

    @Inject
    public BroadcastReceiverInjectionNodeFactory(@Assisted ASTType astType,
                                                 AnalysisContextFactory analysisContextFactory,
                                                 InjectionPointFactory injectionPointFactory,
                                                 InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory,
                                                 InjectionNodeBuilderRepository injectionNodeBuilderRepository,
                                                 InjectionBindingBuilder injectionBindingBuilder) {
        this.analysisContextFactory = analysisContextFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.variableBuilderRepositoryFactory = variableBuilderRepositoryFactory;
        this.injectionNodeBuilderRepository = injectionNodeBuilderRepository;
        this.astType = astType;
        this.injectionBindingBuilder = injectionBindingBuilder;
    }

    @Override
    public InjectionNode buildInjectionNode(MethodDescriptor onCreateMethodDescriptor) {
        AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(onCreateMethodDescriptor));
        return injectionPointFactory.buildInjectionNode(astType, context);
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(MethodDescriptor methodDescriptor) {

        injectionNodeBuilderRepository.putType(Context.class, injectionBindingBuilder.buildThis(Context.class));
        injectionNodeBuilderRepository.putType(android.content.BroadcastReceiver.class, injectionBindingBuilder.buildThis(android.content.BroadcastReceiver.class));

        for (Map.Entry<ASTParameter, TypedExpression> parameterEntry : methodDescriptor.getParameters().entrySet()) {
            injectionNodeBuilderRepository.putType(parameterEntry.getKey().getASTType(), new StaticExpressionNodeBuilder(parameterEntry.getValue()));
            injectionNodeBuilderRepository.putType(parameterEntry.getKey().getASTType(), injectionBindingBuilder.buildExpression(parameterEntry.getValue()));
        }

        variableBuilderRepositoryFactory.addApplicationInjections(injectionNodeBuilderRepository);

        return injectionNodeBuilderRepository;
    }
}
