package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

import static org.androidtransfuse.gen.GeneratedClassAnnotator.annotateGeneratedClass;

/**
 * @author John Ericksen
 */
@Singleton
public class ProviderGenerator {

    private static final String GET_METHOD = "get";
    private static final String PROVIDER_EXT = "_Provider";

    private final Map<String, JDefinedClass> providerClasses = new HashMap<String, JDefinedClass>();
    private final JCodeModel codeModel;
    private final InjectionFragmentGenerator injectionFragmentGenerator;

    @Inject
    public ProviderGenerator(JCodeModel codeModel, InjectionFragmentGenerator injectionFragmentGenerator) {
        this.codeModel = codeModel;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
    }

    public JDefinedClass generateProvider(InjectionNode injectionNode) {

        if (!providerClasses.containsKey(injectionNode.getClassName())) {
            JDefinedClass providerClass = innerGenerateProvider(injectionNode);
            providerClasses.put(injectionNode.getClassName(), providerClass);
        }

        return providerClasses.get(injectionNode.getClassName());
    }

    private JDefinedClass innerGenerateProvider(InjectionNode injectionNode) {

        try {
            JClass injectionNodeClassRef = codeModel.ref(injectionNode.getClassName());

            JPackage jPackage = codeModel._package(injectionNode.getASTType().getPackageClass().getPackage());
            JDefinedClass providerClass = jPackage._class(injectionNode.getASTType().getPackageClass().append(PROVIDER_EXT).getClassName());

            annotateGeneratedClass(providerClass);

            providerClass._implements(codeModel.ref(Provider.class).narrow(injectionNodeClassRef));

            //todo:possible context variable injections?
            //get() method
            JMethod getMethod = providerClass.method(JMod.PUBLIC, injectionNodeClassRef, GET_METHOD);

            JBlock getMethodBody = getMethod.body();

            Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(getMethodBody, providerClass, injectionNode);

            getMethodBody._return(expressionMap.get(injectionNode).getExpression());

            return providerClass;

        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Error while creating provider", e);
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Error while creating provider", e);
        }
    }
}
