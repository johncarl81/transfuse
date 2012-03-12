package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.InjectionInvocationBuilder;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.gen.proxy.AOPProxyGenerator;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableInjectionBuilder implements VariableBuilder {

    private JCodeModel codeModel;
    private UniqueVariableNamer variableNamer;
    private InjectionInvocationBuilder injectionInvocationBuilder;
    private AOPProxyGenerator aopProxyGenerator;
    private InjectionExpressionBuilder injectionExpressionBuilder;

    @Inject
    public VariableInjectionBuilder(JCodeModel codeModel,
                                    UniqueVariableNamer variableNamer,
                                    InjectionInvocationBuilder injectionInvocationBuilder,
                                    AOPProxyGenerator aopProxyGenerator,
                                    InjectionExpressionBuilder injectionExpressionBuilder) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
        this.injectionInvocationBuilder = injectionInvocationBuilder;
        this.aopProxyGenerator = aopProxyGenerator;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JVar variableRef;
        try {

            //AOP proxy (if applicable).  This method will return the injectionNode (without proxying) if no AOPProxyAspect exists
            InjectionNode proxyableInjectionNode = aopProxyGenerator.generateProxy(injectionNode);

            injectionExpressionBuilder.setupInjectionRequirements(injectionBuilderContext, proxyableInjectionNode);

            JType nodeType = codeModel.parseType(proxyableInjectionNode.getClassName());

            ASTInjectionAspect injectionAspect = proxyableInjectionNode.getAspect(ASTInjectionAspect.class);
            if (injectionAspect.getAssignmentType().equals(ASTInjectionAspect.InjectionAssignmentType.LOCAL)) {
                variableRef = injectionBuilderContext.getBlock().decl(nodeType, variableNamer.generateName(proxyableInjectionNode.getClassName()));
            } else {
                variableRef = injectionBuilderContext.getDefinedClass().field(JMod.PRIVATE, nodeType, variableNamer.generateName(proxyableInjectionNode.getClassName()));
            }
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
            throw new TransfuseAnalysisException("Unable to parse class: " + injectionNode.getClassName(), e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("JClassAlreadyExistsException while generating injection: " + injectionNode.getClassName(), e);
        }

        return variableRef;
    }
}
