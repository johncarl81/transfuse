package org.androidtransfuse.gen;

import com.sun.codemodel.*;
import org.androidtransfuse.TransfuseAnnotationProcessor;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResource;

import javax.annotation.Generated;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.text.DateFormat;
import java.util.Date;
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

    @Inject
    public ProviderGenerator(JCodeModel codeModel, InjectionFragmentGenerator injectionFragmentGenerator) {
        this.codeModel = codeModel;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
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

            providerClass.annotate(Generated.class)
                    .param("value", TransfuseAnnotationProcessor.class.getName())
                    .param("date", DateFormat.getInstance().format(new Date()));

            providerClass._implements(Provider.class).narrow(injectionNodeClassRef);

            //JFieldVar applicationField = providerClass.field(JMod.PRIVATE, Application.class, "application");

            JMethod constructor = providerClass.constructor(JMod.PUBLIC);

            //JVar applicationParameter = constructor.param(codeModel.ref(Application.class), "application");

            //constructor.body().assign(JExpr._this().ref(applicationField), applicationParameter);

            //get() method
            JMethod getMethod = providerClass.method(JMod.PUBLIC, injectionNodeClassRef, GET_METHOD);

            JBlock getMethodBody = getMethod.body();

            Map<InjectionNode, JExpression> expressionMap = injectionFragmentGenerator.buildFragment(getMethodBody, providerClass, injectionNode, rResource);

            getMethodBody._return(expressionMap.get(injectionNode));

            return providerClass;

        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Error while creating provider", e);
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Error while creating provider", e);
        }
    }
}
