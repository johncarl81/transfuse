package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionNodeFactoryImpl implements InjectionNodeFactory {

    private ASTType astType;
    private AnalysisContext context;
    private InjectionPointFactory injectionPointFactory;
    private InjectionBindingBuilder injectionBindingBuilder;

    @Inject
    public InjectionNodeFactoryImpl(@Assisted ASTType astType,
                                    @Assisted AnalysisContext context,
                                    InjectionPointFactory injectionPointFactory,
                                    InjectionBindingBuilder injectionBindingBuilder) {
        this.astType = astType;
        this.context = context;
        this.injectionPointFactory = injectionPointFactory;
        this.injectionBindingBuilder = injectionBindingBuilder;
    }

    @Override
    public InjectionNode buildInjectionNode(MethodDescriptor onCreateMethodDescriptor) {
        buildVariableBuilderMap(onCreateMethodDescriptor, context.getInjectionNodeBuilders());

        return injectionPointFactory.buildInjectionNode(astType, context);
    }

    private void buildVariableBuilderMap(MethodDescriptor methodDescriptor, InjectionNodeBuilderRepository injectionNodeBuilders) {

        for (Map.Entry<ASTType, TypedExpression> parameterEntry : methodDescriptor.getTypeMap().entrySet()) {
            injectionNodeBuilders.putType(parameterEntry.getKey(), injectionBindingBuilder.buildExpression(parameterEntry.getValue()));
        }
    }
}
