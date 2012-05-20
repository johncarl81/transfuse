package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTField;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ListenerRegistration;
import org.androidtransfuse.analysis.astAnalyzer.RegistrationAspect;
import org.androidtransfuse.gen.ComponentDescriptor;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.util.InjectionUtil;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ListenerRegistrationGenerator implements ExpressionVariableDependentGenerator {

    private InjectionFragmentGenerator injectionFragmentGenerator;
    private JCodeModel codeModel;

    @Inject
    public ListenerRegistrationGenerator(InjectionFragmentGenerator injectionFragmentGenerator, JCodeModel codeModel) {
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.codeModel = codeModel;
    }

    @Override
    public void generate(JDefinedClass definedClass, JBlock block, Map<InjectionNode, JExpression> expressionMap, ComponentDescriptor descriptor, RResource rResource) {

        try {
            //add listener registration
            for (Map.Entry<InjectionNode, JExpression> injectionNodeJExpressionEntry : expressionMap.entrySet()) {
                if (injectionNodeJExpressionEntry.getKey().containsAspect(RegistrationAspect.class)) {
                    RegistrationAspect registrationAspect = injectionNodeJExpressionEntry.getKey().getAspect(RegistrationAspect.class);

                    buildFieldRegistration(registrationAspect.getFieldRegistrations(), block, definedClass, rResource, injectionNodeJExpressionEntry.getValue());
                    buildMethodRegistration(registrationAspect.getMethodRegistrations(), block, definedClass, rResource, injectionNodeJExpressionEntry.getValue());
                    buildTypeRegistration(registrationAspect.getTypeRegistrations(), block, definedClass, rResource, injectionNodeJExpressionEntry.getValue());
                }
            }
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("TransfuseAnalysisException while trying to build listener registration", e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("NoSucMethodException while trying to build listener registration", e);
        }
    }

    private void buildTypeRegistration(Set<ListenerRegistration<ASTType>> typeRegistrations, JBlock block, JDefinedClass definedClass, RResource rResource, JExpression variableExpression) throws ClassNotFoundException, JClassAlreadyExistsException {
        for (ListenerRegistration<ASTType> typeRegistration : typeRegistrations) {

            Map<InjectionNode, JExpression> viewExpressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, typeRegistration.getViewInjectionNode(), rResource);

            JExpression viewExpression = viewExpressionMap.get(typeRegistration.getViewInjectionNode());

            for (String listenerMethod : typeRegistration.getMethods()) {
                block.invoke(viewExpression, listenerMethod)
                        .arg(variableExpression);
            }
        }
    }

    private void buildMethodRegistration(Set<ListenerRegistration<ASTMethod>> methodRegistrations, JBlock block, JDefinedClass definedClass, RResource rResource, JExpression variableExpression) throws ClassNotFoundException, JClassAlreadyExistsException {
        for (ListenerRegistration<ASTMethod> methodRegistration : methodRegistrations) {

            Map<InjectionNode, JExpression> viewExpressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, methodRegistration.getViewInjectionNode(), rResource);

            JExpression viewExpression = viewExpressionMap.get(methodRegistration.getViewInjectionNode());

            for (String listenerMethod : methodRegistration.getMethods()) {
                block.invoke(viewExpression, listenerMethod)
                        .arg(variableExpression.invoke(methodRegistration.getASTBase().getName()));
            }
        }
    }

    private void buildFieldRegistration(Set<ListenerRegistration<ASTField>> fieldRegistrations, JBlock block, JDefinedClass definedClass, RResource rResource, JExpression variableExpression) throws ClassNotFoundException, JClassAlreadyExistsException {
        for (ListenerRegistration<ASTField> listenerRegistration : fieldRegistrations) {

            Map<InjectionNode, JExpression> viewExpressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, listenerRegistration.getViewInjectionNode(), rResource);

            JExpression viewExpression = viewExpressionMap.get(listenerRegistration.getViewInjectionNode());

            for (String listenerMethod : listenerRegistration.getMethods()) {
                block.invoke(viewExpression, listenerMethod).arg(codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.GET_INSTANCE_METHOD)
                        .invoke(InjectionUtil.GET_FIELD_METHOD)
                        .arg(variableExpression)
                        .arg(JExpr.lit(0))
                        .arg(listenerRegistration.getASTBase().getName())
                        .arg(codeModel.ref(listenerRegistration.getASTBase().getASTType().getName()).staticRef("class")));
            }
        }
    }
}
