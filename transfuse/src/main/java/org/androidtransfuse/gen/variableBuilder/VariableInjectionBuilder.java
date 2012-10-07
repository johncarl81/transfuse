package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTVoidType;
import org.androidtransfuse.analysis.astAnalyzer.AOPProxyAspect;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.gen.*;
import org.androidtransfuse.gen.proxy.AOPProxyGenerator;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableInjectionBuilder implements VariableBuilder {

    private final JCodeModel codeModel;
    private final UniqueVariableNamer variableNamer;
    private final InvocationBuilder injectionInvocationBuilder;
    private final AOPProxyGenerator aopProxyGenerator;
    private final InjectionExpressionBuilder injectionExpressionBuilder;
    private final TypedExpressionFactory typedExpressionFactory;
    private final ExceptionWrapper exceptionWrapper;

    @Inject
    public VariableInjectionBuilder(JCodeModel codeModel,
                                    UniqueVariableNamer variableNamer,
                                    InvocationBuilder injectionInvocationBuilder,
                                    AOPProxyGenerator aopProxyGenerator,
                                    InjectionExpressionBuilder injectionExpressionBuilder,
                                    TypedExpressionFactory typedExpressionFactory,
                                    ExceptionWrapper exceptionWrapper) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
        this.injectionInvocationBuilder = injectionInvocationBuilder;
        this.aopProxyGenerator = aopProxyGenerator;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.typedExpressionFactory = typedExpressionFactory;
        this.exceptionWrapper = exceptionWrapper;
    }

    @Override
    public TypedExpression buildVariable(final InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        final JVar variableRef;
        try {

            //AOP proxy (if applicable).  This method will return the injectionNode (without proxying) if no AOPProxyAspect exists
            final InjectionNode proxyableInjectionNode = injectionNode.containsAspect(AOPProxyAspect.class) ?
                    aopProxyGenerator.generateProxy(injectionNode) : injectionNode;

            injectionExpressionBuilder.setupInjectionRequirements(injectionBuilderContext, proxyableInjectionNode);

            final JType nodeType = codeModel.parseType(proxyableInjectionNode.getClassName());

            final ASTInjectionAspect injectionAspect = proxyableInjectionNode.getAspect(ASTInjectionAspect.class);
            JBlock block = injectionBuilderContext.getBlock();

            if (injectionAspect == null) {
                throw new TransfuseAnalysisException("Injection node not mapped: " + proxyableInjectionNode.getClassName());
            }
            else if (injectionNode.getAspect(ASTInjectionAspect.class).getConstructorInjectionPoints().isEmpty()) {
                throw new TransfuseAnalysisException("No-Arg Constructor required for injection point: " + injectionNode.getClassName());
            }
            else {
                variableRef = exceptionWrapper.wrapException(block,
                        injectionAspect.getConstructorInjectionPoint().getThrowsTypes(),
                        new ExceptionWrapper.BlockWriter<JVar>() {
                            @Override
                            public JVar write(JBlock block) throws ClassNotFoundException {

                                //constructor injection
                                JExpression constructionExpression = injectionInvocationBuilder.buildConstructorCall(
                                        injectionBuilderContext.getVariableMap(),
                                        injectionAspect.getConstructorInjectionPoint(),
                                        nodeType);

                                if (injectionAspect.getAssignmentType().equals(ASTInjectionAspect.InjectionAssignmentType.LOCAL)) {
                                    return injectionBuilderContext.getBlock().decl(nodeType, variableNamer.generateName(proxyableInjectionNode), constructionExpression);
                                } else {
                                    JVar variableRef = injectionBuilderContext.getDefinedClass().field(JMod.PRIVATE, nodeType, variableNamer.generateName(proxyableInjectionNode));
                                    block.assign(variableRef, constructionExpression);
                                    return variableRef;
                                }
                            }
                        });
            }

            //field injection
            for (FieldInjectionPoint fieldInjectionPoint : injectionAspect.getFieldInjectionPoints()) {
                block.add(
                        injectionInvocationBuilder.buildFieldSet(
                                injectionBuilderContext.getVariableMap().get(fieldInjectionPoint.getInjectionNode()),
                                fieldInjectionPoint,
                                variableRef));
            }

            //method injection
            for (final MethodInjectionPoint methodInjectionPoint : injectionAspect.getMethodInjectionPoints()) {
                exceptionWrapper.wrapException(block,
                        methodInjectionPoint.getThrowsTypes(),
                        new ExceptionWrapper.BlockWriter<Void>() {
                            @Override
                            public Void write(JBlock block) throws ClassNotFoundException, JClassAlreadyExistsException {
                                block.add(
                                        injectionInvocationBuilder.buildMethodCall(
                                                ASTVoidType.VOID,
                                                injectionBuilderContext.getVariableMap(),
                                                methodInjectionPoint,
                                                variableRef));
                                return null;
                            }
                        });
            }

        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Unable to parse class: " + injectionNode.getClassName(), e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("JClassAlreadyExistsException while generating injection: " + injectionNode.getClassName(), e);
        }

        return typedExpressionFactory.build(injectionNode.getASTType(), variableRef);
    }
}
