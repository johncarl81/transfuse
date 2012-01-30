package org.androidrobotics.gen.variableBuilder;

import com.sun.codemodel.*;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.gen.InjectionInvocationBuilder;
import org.androidrobotics.gen.UniqueVariableNamer;
import org.androidrobotics.gen.proxy.AOPProxyGenerator;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.MethodInjectionPoint;

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

            //AOP proxy (if applicable).  This method will return the injectionNode (without proxying) if no AOPProxyAspect exists
            InjectionNode proxyableInjectionNode = aopProxyGenerator.generateProxy(injectionNode);

            injectionBuilderContext.setupInjectionRequirements(proxyableInjectionNode);

            JType nodeType = codeModel.parseType(proxyableInjectionNode.getClassName());

            variableRef = injectionBuilderContext.getDefinedClass().field(JMod.PRIVATE, nodeType, variableNamer.generateName(proxyableInjectionNode.getClassName()));

            ASTInjectionAspect injectionAspect = proxyableInjectionNode.getAspect(ASTInjectionAspect.class);
            JBlock block = injectionBuilderContext.getBlock();

            //constructor injection
            block.assign(variableRef,
                    injectionInvocationBuilder.buildConstructorInjection(
                            injectionBuilderContext.getVariableMap(),
                            injectionAspect.getConstructorInjectionPoint(),
                            nodeType));

            //field injection
            for (FieldInjectionPoint fieldInjectionPoint : injectionAspect.getFieldInjectionPoints()) {
                block.add(
                        injectionInvocationBuilder.buildFieldInjection(
                                injectionBuilderContext.getVariableMap(),
                                fieldInjectionPoint,
                                variableRef));
            }

            //method injection
            for (MethodInjectionPoint methodInjectionPoint : injectionAspect.getMethodInjectionPoints()) {
                block.add(
                        injectionInvocationBuilder.buildMethodInjection(
                                injectionBuilderContext.getVariableMap(),
                                methodInjectionPoint,
                                variableRef));
            }

        } catch (ClassNotFoundException e) {
            throw new RoboticsAnalysisException("Unable to parse class: " + injectionNode.getClassName(), e);
        } catch (JClassAlreadyExistsException e) {
            throw new RoboticsAnalysisException("JClassAlreadyExistsException while generating injection: " + injectionNode.getClassName(), e);
        }

        return variableRef;
    }
}
