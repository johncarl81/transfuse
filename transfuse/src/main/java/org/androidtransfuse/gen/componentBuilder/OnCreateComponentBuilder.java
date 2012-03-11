package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.astAnalyzer.MethodCallbackAspect;
import org.androidtransfuse.gen.ComponentBuilder;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResource;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class OnCreateComponentBuilder implements ComponentBuilder {

    private InjectionNode injectionNode;
    private JCodeModel codeModel;
    private InjectionFragmentGenerator injectionFragmentGenerator;
    private Set<MethodCallbackGenerator> methodCallbackGenerators = new HashSet<MethodCallbackGenerator>();

    @Inject
    public OnCreateComponentBuilder(@Assisted InjectionNode injectionNode, InjectionFragmentGenerator injectionFragmentGenerator, JCodeModel codeModel) {
        this.injectionNode = injectionNode;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.codeModel = codeModel;
    }

    @Override
    public void build(JDefinedClass definedClass, RResource rResource) {


        try {
            if (injectionNode != null) {

                final JMethod onCreateMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, "onCreate");
                JBlock block = onCreateMethod.body();
                block.invoke(JExpr._super(), onCreateMethod);

                Map<InjectionNode, JExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, injectionNode, rResource);

                for (Map.Entry<InjectionNode, JExpression> injectionNodeJExpressionEntry : expressionMap.entrySet()) {
                    MethodCallbackAspect methodCallbackAspect = injectionNodeJExpressionEntry.getKey().getAspect(MethodCallbackAspect.class);

                    if (methodCallbackAspect != null && methodCallbackAspect.contains("onCreate")) {
                        Set<MethodCallbackAspect.MethodCallback> methods = methodCallbackAspect.getMethodCallbacks("onCreate");

                        for (MethodCallbackAspect.MethodCallback methodCallback : methods) {
                            //todo: non-public access
                            block.add(injectionNodeJExpressionEntry.getValue().invoke(methodCallback.getMethod().getName()));
                        }
                    }
                }

                for (MethodCallbackGenerator methodCallbackGenerator : methodCallbackGenerators) {
                    methodCallbackGenerator.addLifecycleMethod(definedClass, expressionMap);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("ClassNotFoundException while building Injection Fragment", e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("JClassAlreadyExistsException while building Injection Fragment", e);
        }

    }

    public void addMethodCallbackBuilder(MethodCallbackGenerator methodCallbackGenerator) {
        methodCallbackGenerators.add(methodCallbackGenerator);
    }
}
