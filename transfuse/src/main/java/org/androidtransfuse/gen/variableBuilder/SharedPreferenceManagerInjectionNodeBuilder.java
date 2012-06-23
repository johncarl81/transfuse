package org.androidtransfuse.gen.variableBuilder;

import android.app.Activity;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SharedPreferenceManagerInjectionNodeBuilder extends InjectionNodeBuilderNoAnnotationAdapter{

    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private InjectionPointFactory injectionPointFactory;

    @Inject
    public SharedPreferenceManagerInjectionNodeBuilder(VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                                       InjectionPointFactory injectionPointFactory) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.injectionPointFactory = injectionPointFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        InjectionNode injectionNode = new InjectionNode(astType);

        InjectionNode contextInjectionNode = injectionPointFactory.buildInjectionNode(Activity.class, context);

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildSharedPreferenceManagerVariableBuilder(contextInjectionNode));

        return injectionNode;
    }
}
