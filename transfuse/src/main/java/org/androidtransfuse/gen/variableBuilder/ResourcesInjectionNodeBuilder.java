package org.androidtransfuse.gen.variableBuilder;

import android.app.Application;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ResourcesInjectionNodeBuilder extends InjectionNodeBuilderNoAnnotationAdapter {

    private ASTClassFactory astClassFactory;
    private InjectionPointFactory injectionPointFactory;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public ResourcesInjectionNodeBuilder(ASTClassFactory astClassFactory,
                                         InjectionPointFactory injectionPointFactory,
                                         VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.astClassFactory = astClassFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        InjectionNode injectionNode = new InjectionNode(astType);

        ASTType applicationType = astClassFactory.buildASTClassType(Application.class);
        InjectionNode applicationInjectionNode = injectionPointFactory.buildInjectionNode(applicationType, context);

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildResourcesVariableBuilder(applicationInjectionNode));

        return injectionNode;
    }
}
