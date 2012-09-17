package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;

import static org.androidtransfuse.gen.GeneratedClassAnnotator.annotateGeneratedClass;

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

        JType providedType = codeModel.parseType(injectionNode.getUsageType().getName());

        annotateGeneratedClass(definedClass);

        definedClass._implements(codeModel.ref(Provider.class).narrow(providedType));

        JMethod getMethod = definedClass.method(JMod.PUBLIC, providedType, "get");

        JBlock block = getMethod.body();
        Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, injectionNode);

        block._return(expressionMap.get(injectionNode).getExpression());
    }
}
