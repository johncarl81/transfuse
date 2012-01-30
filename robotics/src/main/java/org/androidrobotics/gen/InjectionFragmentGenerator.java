package org.androidrobotics.gen;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.r.RResource;

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

    public Map<InjectionNode, JExpression> buildFragment(JBlock block, JDefinedClass definedClass, InjectionNode injectionNode, RResource rResource) throws ClassNotFoundException, JClassAlreadyExistsException {

        Map<InjectionNode, JExpression> nodeVariableMap = new HashMap<InjectionNode, JExpression>();
        InjectionBuilderContext injectionBuilderContext = injectionBuilderContextFactory.buildContext(nodeVariableMap, block, definedClass, rResource);

        injectionBuilderContext.buildVariable(injectionNode);

        return nodeVariableMap;
    }
}
