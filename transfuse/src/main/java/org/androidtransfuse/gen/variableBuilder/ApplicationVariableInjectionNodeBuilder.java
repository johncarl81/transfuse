package org.androidtransfuse.gen.variableBuilder;

import android.content.Context;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ApplicationVariableInjectionNodeBuilder extends InjectionNodeBuilderNoAnnotationAdapter {

    private InjectionPointFactory injectionPointFactory;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public ApplicationVariableInjectionNodeBuilder(InjectionPointFactory injectionPointFactory, VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.injectionPointFactory = injectionPointFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        InjectionNode injectionNode = new InjectionNode(astType);

        InjectionNode contextInjectionNode = injectionPointFactory.buildInjectionNode(Context.class, context);

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildApplicationVariableBuilder(contextInjectionNode));

        return injectionNode;
    }
}
