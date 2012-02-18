package org.androidtransfuse.gen;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResource;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionFragmentGenerator {

    private InjectionBuilderContextFactory injectionBuilderContextFactory;
    private InjectionVariableBuilder injectionVariableBuilder;

    @Inject
    public InjectionFragmentGenerator(InjectionBuilderContextFactory injectionBuilderContextFactory, InjectionVariableBuilder injectionVariableBuilder) {
        this.injectionBuilderContextFactory = injectionBuilderContextFactory;
        this.injectionVariableBuilder = injectionVariableBuilder;
    }

    public Map<InjectionNode, JExpression> buildFragment(JBlock block, JDefinedClass definedClass, InjectionNode injectionNode, RResource rResource) throws ClassNotFoundException, JClassAlreadyExistsException {

        Map<InjectionNode, JExpression> nodeVariableMap = new HashMap<InjectionNode, JExpression>();
        InjectionBuilderContext injectionBuilderContext = injectionBuilderContextFactory.buildContext(nodeVariableMap, block, definedClass, rResource);

        injectionVariableBuilder.buildVariable(injectionBuilderContext, injectionNode);

        return nodeVariableMap;
    }
}
