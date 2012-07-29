package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.astAnalyzer.RegistrationAspect;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import java.util.Map;

/**
 * @author John Ericksen
 */
public class ListenerRegistrationGenerator implements ExpressionVariableDependentGenerator {

    @Override
    public void generate(JDefinedClass definedClass, MethodDescriptor methodDescriptor, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor) {

        JBlock block = methodDescriptor.getMethod().body();

        //add listener registration
        for (Map.Entry<InjectionNode, TypedExpression> injectionNodeJExpressionEntry : expressionMap.entrySet()) {
            if (injectionNodeJExpressionEntry.getKey().containsAspect(RegistrationAspect.class)) {
                RegistrationAspect registrationAspect = injectionNodeJExpressionEntry.getKey().getAspect(RegistrationAspect.class);

                for (RegistrationGenerator builder : registrationAspect.getRegistrationBuilders()) {
                    builder.build(definedClass, block, injectionNodeJExpressionEntry.getValue());
                }
            }
        }
    }
}
