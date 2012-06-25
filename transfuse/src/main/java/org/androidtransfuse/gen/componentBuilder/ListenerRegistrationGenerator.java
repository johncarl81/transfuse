package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ListenerRegistration;
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

    @Inject
    public ListenerRegistrationGenerator(InjectionFragmentGenerator injectionFragmentGenerator, InvocationBuilder invocationBuilder) {
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.invocationBuilder = invocationBuilder;
    }

    @Override
    public void generate(JDefinedClass definedClass, JBlock block, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor) {

        try {
            //add listener registration
            for (Map.Entry<InjectionNode, TypedExpression> injectionNodeJExpressionEntry : expressionMap.entrySet()) {
                if (injectionNodeJExpressionEntry.getKey().containsAspect(RegistrationAspect.class)) {
                    RegistrationAspect registrationAspect = injectionNodeJExpressionEntry.getKey().getAspect(RegistrationAspect.class);

                    buildFieldRegistration(registrationAspect.getFieldRegistrations(), block, definedClass, injectionNodeJExpressionEntry.getValue());
                    buildMethodRegistration(registrationAspect.getMethodRegistrations(), block, definedClass, injectionNodeJExpressionEntry.getValue());
                    buildTypeRegistration(registrationAspect.getTypeRegistrations(), block, definedClass, injectionNodeJExpressionEntry.getValue());
                }
            }
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("TransfuseAnalysisException while trying to build listener registration", e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while trying to build listener registration", e);
        }
    }

    private void buildTypeRegistration(Set<ListenerRegistration<ASTType>> typeRegistrations, JBlock block, JDefinedClass definedClass, TypedExpression variableExpression) throws ClassNotFoundException, JClassAlreadyExistsException {
        for (ListenerRegistration<ASTType> typeRegistration : typeRegistrations) {

            Map<InjectionNode, TypedExpression> viewExpressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, typeRegistration.getViewInjectionNode());

            JExpression viewExpression = viewExpressionMap.get(typeRegistration.getViewInjectionNode()).getExpression();

            for (String listenerMethod : typeRegistration.getMethods()) {
                block.invoke(viewExpression, listenerMethod)
                        .arg(variableExpression.getExpression());
            }
        }
    }

    private void buildMethodRegistration(Set<ListenerRegistration<ASTMethod>> methodRegistrations, JBlock block, JDefinedClass definedClass, TypedExpression variableExpression) throws ClassNotFoundException, JClassAlreadyExistsException {
        for (ListenerRegistration<ASTMethod> methodRegistration : methodRegistrations) {

            Map<InjectionNode, TypedExpression> viewExpressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, methodRegistration.getViewInjectionNode());

            JExpression viewExpression = viewExpressionMap.get(methodRegistration.getViewInjectionNode()).getExpression();

            for (String listenerMethod : methodRegistration.getMethods()) {
                block.invoke(viewExpression, listenerMethod)
                        .arg(invocationBuilder.buildMethodCall(methodRegistration.getASTBase().getReturnType().getName(), Collections.EMPTY_LIST, Collections.EMPTY_MAP, variableExpression.getExpression(), methodRegistration.getASTBase()));
            }
        }
    }

    private void buildFieldRegistration(Set<ListenerRegistration<ASTField>> fieldRegistrations, JBlock block, JDefinedClass definedClass, TypedExpression variableExpression) throws ClassNotFoundException, JClassAlreadyExistsException {
        for (ListenerRegistration<ASTField> listenerRegistration : fieldRegistrations) {

            Map<InjectionNode, TypedExpression> viewExpressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, listenerRegistration.getViewInjectionNode());

            JExpression viewExpression = viewExpressionMap.get(listenerRegistration.getViewInjectionNode()).getExpression();

            for (String listenerMethod : listenerRegistration.getMethods()) {
                block.invoke(viewExpression, listenerMethod)
                        .arg(invocationBuilder.buildFieldGet(listenerRegistration.getASTBase().getASTType().getName(), variableExpression.getExpression(), listenerRegistration.getASTBase().getName(), listenerRegistration.getASTBase().getAccessModifier(), listenerRegistration.getLevel()));
            }
        }
    }
}
