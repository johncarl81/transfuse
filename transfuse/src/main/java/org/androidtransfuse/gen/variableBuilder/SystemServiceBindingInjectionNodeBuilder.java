package org.androidtransfuse.gen.variableBuilder;

import android.content.Context;
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
public class SystemServiceBindingInjectionNodeBuilder implements InjectionNodeBuilder {

    private ASTClassFactory astClassFactory;
    private InjectionPointFactory injectionPointFactory;
    private JCodeModel codeMode;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public SystemServiceBindingInjectionNodeBuilder(ASTClassFactory astClassFactory,
                                                    InjectionPointFactory injectionPointFactory,
                                                    JCodeModel codeMode,
                                                    VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.astClassFactory = astClassFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.codeMode = codeMode;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        String systemService = annotation.getProperty("value", String.class);

        InjectionNode injectionNode = new InjectionNode(astType);

        ASTType contextType = astClassFactory.buildASTClassType(Context.class);
        InjectionNode contextInjectionNode = injectionPointFactory.buildInjectionNode(contextType, context);

        try {
            injectionNode.addAspect(VariableBuilder.class,
                    variableInjectionBuilderFactory.buildSystemServiceVariableBuilder(
                            systemService,
                            codeMode.parseType(astType.getName()),
                            contextInjectionNode));
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Unable to parse type " + astType.getName(), e);
        }

        return injectionNode;
    }
}
