package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.util.EmptyRResource;

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

    public void buildProvider(InjectionNode injectionNode, PackageClass providerPackageClass) throws JClassAlreadyExistsException, ClassNotFoundException {
        JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, providerPackageClass.getFullyQualifiedName(), ClassType.CLASS);

        definedClass._implements(Provider.class);

        JMethod getMethod = definedClass.method(JMod.PUBLIC, codeModel.parseType(injectionNode.getClassName()), "get");

        JBlock block = getMethod.body();
        Map<InjectionNode, JExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, injectionNode, new EmptyRResource());

        block._return(expressionMap.get(injectionNode));
    }
}
