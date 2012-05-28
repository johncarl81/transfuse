package org.androidtransfuse.gen.variableBuilder;

import android.content.Context;
import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SystemServiceInjectionNodeBuilder extends InjectionNodeBuilderNoAnnotationAdapter {

    private ASTClassFactory astClassFactory;
    private InjectionPointFactory injectionPointFactory;
    private String systemService;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public SystemServiceInjectionNodeBuilder(@Assisted String systemService,
                                             ASTClassFactory astClassFactory,
                                             InjectionPointFactory injectionPointFactory,
                                             VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.astClassFactory = astClassFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.systemService = systemService;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        InjectionNode injectionNode = new InjectionNode(astType);

        ASTType contextType = astClassFactory.buildASTClassType(Context.class);
        InjectionNode contextInjectionNode = injectionPointFactory.buildInjectionNode(contextType, context);

        injectionNode.addAspect(VariableBuilder.class,
                variableInjectionBuilderFactory.buildSystemServiceVariableBuilder(
                        systemService,
                        contextInjectionNode));

        return injectionNode;
    }
}
