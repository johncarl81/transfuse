package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.model.TypedExpression;

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
    @Inject
    private GeneratedClassAnnotator generatedClassAnnotater;

    public void buildProvider(InjectionNode injectionNode, PackageClass providerPackageClass) throws JClassAlreadyExistsException, ClassNotFoundException {
        JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, providerPackageClass.getFullyQualifiedName(), ClassType.CLASS);

        generatedClassAnnotater.annotateClass(definedClass);

        definedClass._implements(Provider.class);

        JMethod getMethod = definedClass.method(JMod.PUBLIC, codeModel.parseType(injectionNode.getClassName()), "get");

        JBlock block = getMethod.body();
        Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, injectionNode);

        block._return(expressionMap.get(injectionNode).getExpression());
    }
}
