package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;
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
            InjectionNode proxyableInjectionNode = aopProxyGenerator.generateProxy(injectionNode);

            injectionExpressionBuilder.setupInjectionRequirements(injectionBuilderContext, proxyableInjectionNode);

            final JType nodeType = codeModel.parseType(proxyableInjectionNode.getClassName());

            final ASTInjectionAspect injectionAspect = proxyableInjectionNode.getAspect(ASTInjectionAspect.class);
            if (injectionAspect.getAssignmentType().equals(ASTInjectionAspect.InjectionAssignmentType.LOCAL)) {
                variableRef = injectionBuilderContext.getBlock().decl(nodeType, variableNamer.generateName(proxyableInjectionNode));
            } else {
                variableRef = injectionBuilderContext.getDefinedClass().field(JMod.PRIVATE, nodeType, variableNamer.generateName(proxyableInjectionNode));
            }
            JBlock block = injectionBuilderContext.getBlock();

            if (injectionNode.getAspect(ASTInjectionAspect.class).getConstructorInjectionPoints().isEmpty()) {
                throw new TransfuseAnalysisException("No-Arg Constructor required for injection point: " + injectionNode.getClassName());
            }

            //constructor injection
            wrapExceptionHandling(block,
                    injectionAspect.getConstructorInjectionPoint().getThrowsTypes(),
                    new BlockWriter() {
                        @Override
                        public void write(JBlock block) throws ClassNotFoundException {
                            block.assign(variableRef,
                                    injectionInvocationBuilder.buildConstructorCall(
                                            injectionBuilderContext.getVariableMap(),
                                            injectionAspect.getConstructorInjectionPoint(),
                                            nodeType));
                        }
                    });

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
                        new BlockWriter() {
                            @Override
                            public void write(JBlock block) throws ClassNotFoundException, JClassAlreadyExistsException {
                                block.add(
                                        injectionInvocationBuilder.buildMethodCall(
                                                Object.class.getName(),
                                                injectionBuilderContext.getVariableMap(),
                                                methodInjectionPoint,
                                                variableRef));
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

    private void wrapExceptionHandling(JBlock block, List<ASTType> throwsTypes, BlockWriter blockWriter) throws ClassNotFoundException, JClassAlreadyExistsException {
        JTryBlock tryBlock = null;
        JBlock writeBlock = block;
        if (throwsTypes.size() > 0) {
            tryBlock = block._try();
            writeBlock = tryBlock.body();
        }

        blockWriter.write(writeBlock);

        if (tryBlock != null) {
            for (ASTType throwsType : throwsTypes) {
                JCatchBlock catchBlock = tryBlock._catch(codeModel.ref(throwsType.getName()));
                JVar exceptionParam = catchBlock.param("e");

                catchBlock.body()._throw(JExpr._new(codeModel.ref(TransfuseInjectionException.class))
                        .arg(JExpr.lit(throwsType.getName() + " while performing dependency injection"))
                        .arg(exceptionParam));
            }
        }
    }

    private interface BlockWriter {
        void write(JBlock block) throws ClassNotFoundException, JClassAlreadyExistsException;
    }
}
