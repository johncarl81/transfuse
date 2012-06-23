package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class DependentInjectionNodeBuilder implements InjectionNodeBuilder{

    private Class dependency;
    private DependentVariableBuilder variableBuilder;
    private InjectionPointFactory injectionPointFactory;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public DependentInjectionNodeBuilder(@Assisted Class dependency,
                                         @Assisted DependentVariableBuilder variableBuilder,
                                         InjectionPointFactory injectionPointFactory,
                                         VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.dependency = dependency;
        this.variableBuilder = variableBuilder;
        this.injectionPointFactory = injectionPointFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, Collection<ASTAnnotation> annotations) {
        InjectionNode injectionNode = new InjectionNode(astType);

        InjectionNode contextInjectionNode = injectionPointFactory.buildInjectionNode(dependency, context);

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildDependentVariableBuilderWrapper(contextInjectionNode, variableBuilder, Object.class));

        return injectionNode;
    }
}
