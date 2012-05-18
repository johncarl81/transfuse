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

                    for (ListenerRegistration<ASTField> listenerRegistration : registrationAspect.getFieldRegistrations()) {

                        Map<InjectionNode, JExpression> viewExpressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, listenerRegistration.getViewInjectionNode(), rResource);

                        JExpression viewExpression = viewExpressionMap.get(listenerRegistration.getViewInjectionNode());

                        for (String listenerMethod : listenerRegistration.getMethods()) {
                            block.invoke(viewExpression, listenerMethod).arg(codeModel.ref(InjectionUtil.class).staticInvoke(InjectionUtil.GET_INSTANCE_METHOD)
                                    .invoke(InjectionUtil.GET_FIELD_METHOD)
                                    .arg(injectionNodeJExpressionEntry.getValue())
                                    .arg(JExpr.lit(0))
                                    .arg(listenerRegistration.getASTBase().getName())
                                    .arg(codeModel.ref(listenerRegistration.getASTBase().getASTType().getName()).staticRef("class")));
                        }
                    }

                    for (ListenerRegistration<ASTMethod> methodRegistration : registrationAspect.getMethodRegistrations()) {

                        Map<InjectionNode, JExpression> viewExpressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, methodRegistration.getViewInjectionNode(), rResource);

                        JExpression viewExpression = viewExpressionMap.get(methodRegistration.getViewInjectionNode());

                        JExpression instanceExpression = injectionNodeJExpressionEntry.getValue();

                        for (String listenerMethod : methodRegistration.getMethods()) {
                            block.invoke(viewExpression, listenerMethod)
                                    .arg(instanceExpression.invoke(methodRegistration.getASTBase().getName()));
                        }
                    }

                    for (ListenerRegistration<ASTType> typeRegistration : registrationAspect.getTypeRegistrations()) {

                        Map<InjectionNode, JExpression> viewExpressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, typeRegistration.getViewInjectionNode(), rResource);

                        JExpression viewExpression = viewExpressionMap.get(typeRegistration.getViewInjectionNode());

                        JExpression instanceExpression = injectionNodeJExpressionEntry.getValue();

                        for (String listenerMethod : typeRegistration.getMethods()) {
                            block.invoke(viewExpression, listenerMethod)
                                    .arg(instanceExpression);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("TransfuseAnalysisException while trying to build listener registration", e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("NoSucMethodException while trying to build listener registration", e);
        }
    }
}
