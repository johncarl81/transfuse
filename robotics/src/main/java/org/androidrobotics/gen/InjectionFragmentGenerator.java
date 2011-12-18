package org.androidrobotics.gen;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.MethodInjectionPoint;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionFragmentGenerator {

    private InjectionBuilderContextFactory injectionBuilderContextFactory;
    private InjectionInvocationBuilder injectionInvocationBuilder;

    @Inject
    public InjectionFragmentGenerator(InjectionInvocationBuilder injectionInvocationBuilder, InjectionBuilderContextFactory injectionBuilderContextFactory) {
        this.injectionInvocationBuilder = injectionInvocationBuilder;
        this.injectionBuilderContextFactory = injectionBuilderContextFactory;
    }

    public JExpression buildFragment(JBlock block, JDefinedClass definedClass, InjectionNode injectionNode, VariableBuilderRepository variableBuilderMap) throws ClassNotFoundException, JClassAlreadyExistsException {

        Map<InjectionNode, JExpression> nodeVariableMap = new HashMap<InjectionNode, JExpression>();
        InjectionBuilderContext injectionBuilderContext = injectionBuilderContextFactory.buildContext(nodeVariableMap, block, definedClass, variableBuilderMap);

        JExpression variable = injectionBuilderContext.buildVariable(injectionNode);

        for (Map.Entry<InjectionNode, JExpression> nodeEntry : nodeVariableMap.entrySet()) {

            //field injection
            for (FieldInjectionPoint fieldInjectionPoint : nodeEntry.getKey().getFieldInjectionPoints()) {
                block.add(injectionInvocationBuilder.buildParameterInjection(nodeVariableMap, fieldInjectionPoint, nodeEntry.getValue()));
            }

            //method injection
            for (MethodInjectionPoint methodInjectionPoint : nodeEntry.getKey().getMethodInjectionPoints()) {
                block.add(injectionInvocationBuilder.buildMethodInjection(nodeVariableMap, methodInjectionPoint, nodeEntry.getValue()));
            }
        }

        return variable;
    }
}
