package org.androidtransfuse.gen.variableBuilder;

import android.content.Context;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.SystemService;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SystemServiceBindingInjectionNodeBuilder extends InjectionNodeBuilderSingleAnnotationAdapter<SystemService> {

    private ASTClassFactory astClassFactory;
    private InjectionPointFactory injectionPointFactory;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public SystemServiceBindingInjectionNodeBuilder(ASTClassFactory astClassFactory,
                                                    InjectionPointFactory injectionPointFactory,
                                                    VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        super(SystemService.class);
        this.astClassFactory = astClassFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        String systemService = annotation.getProperty("value", String.class);

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
