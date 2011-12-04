package org.androidrobotics.gen;

import com.sun.codemodel.*;
import org.androidrobotics.analysis.RoboticsAnalysisException;

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

    public JExpression innerBuildVariable(String className, InjectionBuilderContext injectionBuilderContext) {
        JVar variableRef;
        try {
            injectionBuilderContext.setupInjectionRequirements();

            JType nodeType = codeModel.parseType(className);

            variableRef = injectionBuilderContext.getDefinedClass().field(JMod.PRIVATE, nodeType, variableNamer.generateName(injectionBuilderContext.getInjectionNode().getClassName()));

            //constructor injection
            injectionBuilderContext.getBlock().assign(variableRef,
                    injectionInvocationBuilder.buildConstructorCall(
                            injectionBuilderContext.getVariableMap(),
                            injectionBuilderContext.getInjectionNode().getConstructorInjectionPoint(),
                            nodeType
                    ));
        } catch (ClassNotFoundException e) {
            throw new RoboticsAnalysisException("Unable to parse class: " + injectionBuilderContext.getInjectionNode().getClassName(), e);
        } catch (JClassAlreadyExistsException e) {
            throw new RoboticsAnalysisException("Class generation tried to generate a class that already existed", e);
        }

        return variableRef;
    }
}
