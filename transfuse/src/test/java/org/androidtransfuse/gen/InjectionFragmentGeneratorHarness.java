package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.TransfuseAnnotationProcessor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.util.EmptyRResource;

import javax.annotation.Generated;
import javax.inject.Inject;
import javax.inject.Provider;
import java.text.DateFormat;
import java.util.Date;
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

        definedClass.annotate(Generated.class)
                .param("value", TransfuseAnnotationProcessor.class.getName())
                .param("date", DateFormat.getInstance().format(new Date()));

        definedClass._implements(Provider.class);

        JMethod getMethod = definedClass.method(JMod.PUBLIC, codeModel.parseType(injectionNode.getClassName()), "get");

        JBlock block = getMethod.body();
        Map<InjectionNode, JExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, injectionNode, new EmptyRResource());

        block._return(expressionMap.get(injectionNode));
    }
}
