package org.androidrobotics.gen.variableBuilder;

import android.content.Context;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.InjectionPointFactory;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.IdentifiedInjectionNode;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;
import javax.lang.model.element.AnnotationValue;

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
        String systemService = (String) ((AnnotationValue) annotation.getProperty("value")).getValue();

        InjectionNode injectionNode = new IdentifiedInjectionNode(astType, systemService);

        ASTType contextType = astClassFactory.buildASTClassType(Context.class);
        InjectionNode contextInjectionNode = injectionPointFactory.buildInjectionNode(contextType, context);

        try {
            injectionNode.addAspect(VariableBuilder.class,
                    variableInjectionBuilderFactory.buildSystemServiceVariableBuilder(
                            systemService,
                            codeMode.parseType(astType.getName()),
                            contextInjectionNode));
        } catch (ClassNotFoundException e) {
            throw new RoboticsAnalysisException("Unable to parse type " + astType.getName(), e);
        }

        return injectionNode;
    }
}
