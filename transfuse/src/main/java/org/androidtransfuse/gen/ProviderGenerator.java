package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.model.r.RResource;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class ProviderGenerator {

    private static final String GET_METHOD = "get";

    private static Map<String, JDefinedClass> providerClasses = new HashMap<String, JDefinedClass>();
    private JCodeModel codeModel;
    private InjectionFragmentGenerator injectionFragmentGenerator;
    private GeneratedClassAnnotator generatedClassAnnotator;

    @Inject
    public ProviderGenerator(JCodeModel codeModel, InjectionFragmentGenerator injectionFragmentGenerator, GeneratedClassAnnotator generatedClassAnnotator) {
        this.codeModel = codeModel;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.generatedClassAnnotator = generatedClassAnnotator;
    }

    public JDefinedClass generateProvider(InjectionNode injectionNode, RResource rResource) {

        if (!providerClasses.containsKey(injectionNode.getClassName())) {
            JDefinedClass providerClass = innerGenerateProvider(injectionNode, rResource);
            providerClasses.put(injectionNode.getClassName(), providerClass);
        }

        return providerClasses.get(injectionNode.getClassName());
    }

    private JDefinedClass innerGenerateProvider(InjectionNode injectionNode, RResource rResource) {

        try {
            JClass injectionNodeClassRef = codeModel.ref(injectionNode.getClassName());

            JDefinedClass providerClass = codeModel._class(JMod.PUBLIC, injectionNode.getClassName() + "_Provider", ClassType.CLASS);

            generatedClassAnnotator.annotateClass(providerClass);

            providerClass._implements(Provider.class).narrow(injectionNodeClassRef);

            //todo:possible context variable injections?
            //get() method
            JMethod getMethod = providerClass.method(JMod.PUBLIC, injectionNodeClassRef, GET_METHOD);

            JBlock getMethodBody = getMethod.body();

            Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(getMethodBody, providerClass, injectionNode, rResource);

            getMethodBody._return(expressionMap.get(injectionNode).getExpression());

            return providerClass;

        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Error while creating provider", e);
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Error while creating provider", e);
        }
    }
}
