package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ActionRegistration;
import org.androidtransfuse.analysis.astAnalyzer.RegistrationAspect;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ListenerRegistrationGenerator implements ExpressionVariableDependentGenerator {

    private InjectionFragmentGenerator injectionFragmentGenerator;
    private InvocationBuilder invocationBuilder;
    private JCodeModel codeModel;

    @Inject
    public ListenerRegistrationGenerator(InjectionFragmentGenerator injectionFragmentGenerator, InvocationBuilder invocationBuilder, JCodeModel codeModel) {
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.invocationBuilder = invocationBuilder;
        this.codeModel = codeModel;
    }

    @Override
    public void generate(JDefinedClass definedClass, MethodDescriptor methodDescriptor, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor) {

        try {
            JBlock block = methodDescriptor.getMethod().body();

            //add listener registration
            for (Map.Entry<InjectionNode, TypedExpression> injectionNodeJExpressionEntry : expressionMap.entrySet()) {
                if (injectionNodeJExpressionEntry.getKey().containsAspect(RegistrationAspect.class)) {
                    RegistrationAspect registrationAspect = injectionNodeJExpressionEntry.getKey().getAspect(RegistrationAspect.class);

                    buildFieldRegistration(registrationAspect.getFieldRegistrations(), block, definedClass, injectionNodeJExpressionEntry.getValue());
                    buildMethodRegistration(registrationAspect.getMethodRegistrations(), block, definedClass, injectionNodeJExpressionEntry.getValue());
                    buildTypeRegistration(registrationAspect.getTypeRegistrations(), block, definedClass, injectionNodeJExpressionEntry.getValue());

                    for (RegistrationGenerator builder : registrationAspect.getRegistrationBuilders()) {
                        builder.build(definedClass, block, injectionNodeJExpressionEntry.getValue());
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("TransfuseAnalysisException while trying to build listener registration", e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while trying to build listener registration", e);
        }
    }

    private void buildTypeRegistration(Set<ActionRegistration<ASTType>> typeRegistrations, JBlock block, JDefinedClass definedClass, TypedExpression variableExpression) throws ClassNotFoundException, JClassAlreadyExistsException {
        for (ActionRegistration<ASTType> typeRegistration : typeRegistrations) {

            Map<InjectionNode, TypedExpression> viewExpressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, typeRegistration.getInjectionNode());

            JExpression viewExpression = viewExpressionMap.get(typeRegistration.getInjectionNode()).getExpression();

            for (ASTMethod listenerMethod : typeRegistration.getMethods()) {
                block.invoke(viewExpression, listenerMethod.getName())
                        .arg(variableExpression.getExpression());
            }
        }
    }

    private void buildMethodRegistration(Set<ActionRegistration<ASTMethod>> methodRegistrations, JBlock block, JDefinedClass definedClass, TypedExpression variableExpression) throws ClassNotFoundException, JClassAlreadyExistsException {
        for (ActionRegistration<ASTMethod> methodRegistration : methodRegistrations) {

            Map<InjectionNode, TypedExpression> viewExpressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, methodRegistration.getInjectionNode());

            JExpression viewExpression = viewExpressionMap.get(methodRegistration.getInjectionNode()).getExpression();

            for (ASTMethod listenerMethod : methodRegistration.getMethods()) {
                block.invoke(viewExpression, listenerMethod.getName())
                        .arg(invocationBuilder.buildMethodCall(methodRegistration.getASTBase().getReturnType().getName(),
                                Collections.EMPTY_LIST,
                                Collections.EMPTY_MAP,
                                variableExpression.getType(),
                                variableExpression.getExpression(),
                                methodRegistration.getASTBase()));
            }
        }
    }

    private void buildFieldRegistration(Set<ActionRegistration<ASTField>> fieldRegistrations, JBlock block, JDefinedClass definedClass, TypedExpression variableExpression) throws ClassNotFoundException, JClassAlreadyExistsException {
        for (ActionRegistration<ASTField> listenerRegistration : fieldRegistrations) {

            Map<InjectionNode, TypedExpression> viewExpressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, listenerRegistration.getInjectionNode());

            JExpression viewExpression = viewExpressionMap.get(listenerRegistration.getInjectionNode()).getExpression();

            for (ASTMethod listenerMethod : listenerRegistration.getMethods()) {
                block.invoke(viewExpression, listenerMethod.getName())
                        .arg(invocationBuilder.buildFieldGet(listenerRegistration.getASTBase().getASTType(),
                                codeModel.ref(variableExpression.getType().getName()),
                                variableExpression.getExpression(),
                                listenerRegistration.getASTBase().getName(),
                                listenerRegistration.getASTBase().getAccessModifier()));
            }
        }
    }
}
