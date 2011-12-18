package org.androidrobotics.gen.variableBuilder;

import com.sun.codemodel.*;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.gen.InjectionInvocationBuilder;
import org.androidrobotics.gen.UniqueVariableNamer;
import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public abstract class VariableInjectionBuilderBase implements VariableBuilder {

    private JCodeModel codeModel;
    private UniqueVariableNamer variableNamer;
    private InjectionInvocationBuilder injectionInvocationBuilder;

    public VariableInjectionBuilderBase(JCodeModel codeModel, UniqueVariableNamer variableNamer, InjectionInvocationBuilder injectionInvocationBuilder) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
        this.injectionInvocationBuilder = injectionInvocationBuilder;
    }

    public JExpression innerBuildVariable(String className, InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JVar variableRef;
        try {
            injectionBuilderContext.setupInjectionRequirements(injectionNode);

            JType nodeType = codeModel.parseType(className);

            variableRef = injectionBuilderContext.getDefinedClass().field(JMod.PRIVATE, nodeType, variableNamer.generateName(injectionNode.getClassName()));

            //assuming that constructor exists
            ASTInjectionAspect injectionAspect = injectionNode.getAspect(ASTInjectionAspect.class);
            //constructor injection
            injectionBuilderContext.getBlock().assign(variableRef,
                    injectionInvocationBuilder.buildConstructorCall(
                            injectionBuilderContext.getVariableMap(),
                            injectionAspect.getConstructorInjectionPoint(),
                            nodeType
                    ));
        } catch (ClassNotFoundException e) {
            throw new RoboticsAnalysisException("Unable to parse class: " + injectionNode.getClassName(), e);
        } catch (JClassAlreadyExistsException e) {
            throw new RoboticsAnalysisException("Class generation tried to generate a class that already existed", e);
        }

        return variableRef;
    }
}
