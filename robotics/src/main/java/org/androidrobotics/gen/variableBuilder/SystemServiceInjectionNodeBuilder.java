package org.androidrobotics.gen.variableBuilder;

import android.content.Context;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.InjectionPointFactory;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

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
            throw new RoboticsAnalysisException("Unable to parse type " + systemServiceClass.getName(), e);
        }

        return injectionNode;
    }
}
