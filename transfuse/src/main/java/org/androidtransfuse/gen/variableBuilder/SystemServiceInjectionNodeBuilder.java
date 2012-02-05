package org.androidtransfuse.gen.variableBuilder;

import android.content.Context;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SystemServiceInjectionNodeBuilder implements InjectionNodeBuilder {

    private ASTClassFactory astClassFactory;
    private InjectionPointFactory injectionPointFactory;
    private JCodeModel codeMode;
    private String systemService;
    private Class<?> systemServiceClass;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public SystemServiceInjectionNodeBuilder(@Assisted String systemService,
                                             @Assisted Class<?> systemServiceClass,
                                             ASTClassFactory astClassFactory,
                                             InjectionPointFactory injectionPointFactory,
                                             JCodeModel codeMode,
                                             VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.astClassFactory = astClassFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.codeMode = codeMode;
        this.systemService = systemService;
        this.systemServiceClass = systemServiceClass;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        InjectionNode injectionNode = new InjectionNode(astType);

        ASTType contextType = astClassFactory.buildASTClassType(Context.class);
        InjectionNode contextInjectionNode = injectionPointFactory.buildInjectionNode(contextType, context);

        try {
            injectionNode.addAspect(VariableBuilder.class,
                    variableInjectionBuilderFactory.buildSystemServiceVariableBuilder(
                            systemService,
                            codeMode.parseType(systemServiceClass.getName()),
                            contextInjectionNode));
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Unable to parse type " + systemServiceClass.getName(), e);
        }

        return injectionNode;
    }
}
