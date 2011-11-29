package org.androidrobotics.gen;

import com.sun.codemodel.*;
import org.androidrobotics.analysis.RoboticsAnalysisException;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableInjectionBuilder implements VariableBuilder {

    private JCodeModel codeModel;
    private UniqueVariableNamer variableNamer;
    private InjectionInvocationBuilder injectionInvocationBuilder;

    @Inject
    public VariableInjectionBuilder(JCodeModel codeModel, UniqueVariableNamer variableNamer, InjectionInvocationBuilder injectionInvocationBuilder) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
        this.injectionInvocationBuilder = injectionInvocationBuilder;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext) {
        JVar variableRef;
        try {
            injectionBuilderContext.setupInjectionRequirements();

            JType nodeType = codeModel.parseType(injectionBuilderContext.getInjectionNode().getClassName());

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
