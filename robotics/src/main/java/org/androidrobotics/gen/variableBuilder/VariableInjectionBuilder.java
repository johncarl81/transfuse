package org.androidrobotics.gen.variableBuilder;

import com.sun.codemodel.*;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.astAnalyzer.AOPProxyAspect;
import org.androidrobotics.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.gen.InjectionInvocationBuilder;
import org.androidrobotics.gen.UniqueVariableNamer;
import org.androidrobotics.gen.proxy.AOPProxyGenerator;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableInjectionBuilder implements VariableBuilder {

    private JCodeModel codeModel;
    private UniqueVariableNamer variableNamer;
    private InjectionInvocationBuilder injectionInvocationBuilder;
    private AOPProxyGenerator aopProxyGenerator;

    @Inject
    public VariableInjectionBuilder(JCodeModel codeModel,
                                    UniqueVariableNamer variableNamer,
                                    InjectionInvocationBuilder injectionInvocationBuilder,
                                    AOPProxyGenerator aopProxyGenerator) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
        this.injectionInvocationBuilder = injectionInvocationBuilder;
        this.aopProxyGenerator = aopProxyGenerator;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JVar variableRef;
        try {

            InjectionNode proxyableInjectionNode = injectionNode;

            //AOP proxy (if applicable)
            if (injectionNode.containsAspect(AOPProxyAspect.class)) {
                proxyableInjectionNode = aopProxyGenerator.generateProxy(injectionNode);
            }

            injectionBuilderContext.setupInjectionRequirements(proxyableInjectionNode);


            JType nodeType = codeModel.parseType(proxyableInjectionNode.getClassName());

            variableRef = injectionBuilderContext.getDefinedClass().field(JMod.PRIVATE, nodeType, variableNamer.generateName(proxyableInjectionNode.getClassName()));

            //assuming that constructor exists
            ASTInjectionAspect injectionAspect = proxyableInjectionNode.getAspect(ASTInjectionAspect.class);
            //constructor injection
            injectionBuilderContext.getBlock().assign(variableRef,
                    injectionInvocationBuilder.buildConstructorCall(
                            injectionBuilderContext.getVariableMap(),
                            injectionAspect.getConstructorInjectionPoint(),
                            nodeType
                    ));
        } catch (ClassNotFoundException e) {
            throw new RoboticsAnalysisException("Unable to parse class: " + injectionNode.getClassName(), e);
        }

        return variableRef;
    }
}
