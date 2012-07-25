package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.AOPProxyAspect;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.gen.proxy.AOPProxyGenerator;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.TransfuseInjectionException;

import javax.inject.Inject;
import java.util.List;

/**
 * @author John Ericksen
 */
public class VariableInjectionBuilder implements VariableBuilder {

    private JCodeModel codeModel;
    private UniqueVariableNamer variableNamer;
    private InvocationBuilder injectionInvocationBuilder;
    private AOPProxyGenerator aopProxyGenerator;
    private InjectionExpressionBuilder injectionExpressionBuilder;
    private TypedExpressionFactory typedExpressionFactory;

    @Inject
    public VariableInjectionBuilder(JCodeModel codeModel,
                                    UniqueVariableNamer variableNamer,
                                    InvocationBuilder injectionInvocationBuilder,
                                    AOPProxyGenerator aopProxyGenerator,
                                    InjectionExpressionBuilder injectionExpressionBuilder,
                                    TypedExpressionFactory typedExpressionFactory) {
        this.codeModel = codeModel;
        this.variableNamer = variableNamer;
        this.injectionInvocationBuilder = injectionInvocationBuilder;
        this.aopProxyGenerator = aopProxyGenerator;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.typedExpressionFactory = typedExpressionFactory;
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
            else {
                variableRef = wrapExceptionHandling(block,
                        injectionAspect.getConstructorInjectionPoint().getThrowsTypes(),
                        new BlockWriter<JVar>() {
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
                            }});
            }


            if (injectionNode.getAspect(ASTInjectionAspect.class).getConstructorInjectionPoints().isEmpty()) {
                throw new TransfuseAnalysisException("No-Arg Constructor required for injection point: " + injectionNode.getClassName());
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
                wrapExceptionHandling(block,
                        methodInjectionPoint.getThrowsTypes(),
                        new BlockWriter<Void>() {
                            @Override
                            public Void write(JBlock block) throws ClassNotFoundException, JClassAlreadyExistsException {
                                block.add(
                                        injectionInvocationBuilder.buildMethodCall(
                                                Object.class.getName(),
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

    private <T> T wrapExceptionHandling(JBlock block, List<ASTType> throwsTypes, BlockWriter<T> blockWriter) throws ClassNotFoundException, JClassAlreadyExistsException {
        JTryBlock tryBlock = null;
        JBlock writeBlock = block;
        if (throwsTypes.size() > 0) {
            tryBlock = block._try();
            writeBlock = tryBlock.body();
        }

        T output = blockWriter.write(writeBlock);

        if (tryBlock != null) {
            for (ASTType throwsType : throwsTypes) {
                JCatchBlock catchBlock = tryBlock._catch(codeModel.ref(throwsType.getName()));
                JVar exceptionParam = catchBlock.param("e");

                catchBlock.body()._throw(JExpr._new(codeModel.ref(TransfuseInjectionException.class))
                        .arg(JExpr.lit(throwsType.getName() + " while performing dependency injection"))
                        .arg(exceptionParam));
            }
        }
        return output;
    }

    private interface BlockWriter<T> {
        T write(JBlock block) throws ClassNotFoundException, JClassAlreadyExistsException;
    }
}
