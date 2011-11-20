package org.androidrobotics.gen;

import com.sun.codemodel.*;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.PackageClass;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionFragmentGeneratorHarness {

    @Inject
    private JCodeModel codeModel;
    @Inject
    private InjectionFragmentGenerator injectionFragmentGenerator;

    public void buildProvider(InjectionNode injectionNode, PackageClass providerPackageClass, Map<String, VariableBuilder> builderMap) throws JClassAlreadyExistsException, ClassNotFoundException {
        JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, providerPackageClass.getFullyQualifiedName(), ClassType.CLASS);

        definedClass._implements(Provider.class);

        JMethod getMethod = definedClass.method(JMod.PUBLIC, codeModel.ref(injectionNode.getClassName()), "get");

        JBlock block = getMethod.body();
        JExpression variable = injectionFragmentGenerator.buildFragment(block, definedClass, injectionNode, builderMap);

        block._return(variable);
    }
}
