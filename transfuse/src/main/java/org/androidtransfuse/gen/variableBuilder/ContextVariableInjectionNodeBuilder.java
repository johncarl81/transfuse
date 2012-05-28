package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ContextVariableInjectionNodeBuilder extends InjectionNodeBuilderNoAnnotationAdapter {

    private Class clazz;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public ContextVariableInjectionNodeBuilder(@Assisted Class clazz, VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.clazz = clazz;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        InjectionNode injectionNode = new InjectionNode(astType);

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildContextVariableBuilder(clazz));

        return injectionNode;
    }
}
