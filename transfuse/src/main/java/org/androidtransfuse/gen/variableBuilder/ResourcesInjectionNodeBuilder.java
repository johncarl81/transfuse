package org.androidtransfuse.gen.variableBuilder;

import android.app.Application;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ResourcesInjectionNodeBuilder implements InjectionNodeBuilder {

    private ASTClassFactory astClassFactory;
    private InjectionPointFactory injectionPointFactory;

    @Inject
    public ResourcesInjectionNodeBuilder(ASTClassFactory astClassFactory, InjectionPointFactory injectionPointFactory) {
        this.astClassFactory = astClassFactory;
        this.injectionPointFactory = injectionPointFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        InjectionNode injectionNode = new InjectionNode(astType);

        ASTType applicationType = astClassFactory.buildASTClassType(Application.class);
        InjectionNode applicationInjectionNode = injectionPointFactory.buildInjectionNode(applicationType, context);

        injectionNode.addAspect(VariableBuilder.class, new ResourcesVariableBuilder(applicationInjectionNode));

        return injectionNode;
    }
}
