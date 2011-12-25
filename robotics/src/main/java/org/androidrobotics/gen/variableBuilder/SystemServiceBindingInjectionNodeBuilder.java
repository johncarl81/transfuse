package org.androidrobotics.gen.variableBuilder;

import android.content.Context;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.InjectionPointFactory;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.FieldInjectionPoint;
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
        InjectionNode injectionNode = new InjectionNode(astType);

        ASTType contextType = astClassFactory.buildASTClassType(Context.class);

        FieldInjectionPoint contextInjectionNode = injectionPointFactory.buildInjectionPoint(contextType, context);

        String systemService = (String) ((AnnotationValue) annotation.getProperty("value")).getValue();

        try {
            injectionNode.addAspect(VariableBuilder.class,
                    variableInjectionBuilderFactory.buildSystemServiceVariableBuilder(
                            systemService,
                            codeMode.parseType(astType.getName()),
                            contextInjectionNode.getInjectionNode()));
        } catch (ClassNotFoundException e) {
            throw new RoboticsAnalysisException("Unable to parse type " + astType.getName(), e);
        }

        return injectionNode;
    }
}
