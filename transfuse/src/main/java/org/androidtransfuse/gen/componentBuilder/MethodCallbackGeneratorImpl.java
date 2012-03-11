package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import org.androidtransfuse.analysis.astAnalyzer.MethodCallbackAspect;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.*;

/**
 * @author John Ericksen
 */
public class MethodCallbackGeneratorImpl implements MethodCallbackGenerator {

    private String name;
    private List<Class<?>> methodParameterTypes;
    private MethodGenerator methodGenerator;

    @Inject
    public MethodCallbackGeneratorImpl(@Assisted String name, @Assisted MethodGenerator methodGenerator, @Assisted Class<?>... methodParameterTypes) {
        this.name = name;
        this.methodParameterTypes = new ArrayList<Class<?>>();

        if (methodParameterTypes != null) {
            this.methodParameterTypes.addAll(Arrays.asList(methodParameterTypes));
        }

        this.methodGenerator = methodGenerator;
    }

    @Override
    public void addLifecycleMethod(JDefinedClass definedClass, Map<InjectionNode, JExpression> expressionMap) {
        JMethod method = null;
        for (Map.Entry<InjectionNode, JExpression> injectionNodeJExpressionEntry : expressionMap.entrySet()) {
            MethodCallbackAspect methodCallbackAspect = injectionNodeJExpressionEntry.getKey().getAspect(MethodCallbackAspect.class);

            if (methodCallbackAspect != null && methodCallbackAspect.contains(name)) {
                Set<MethodCallbackAspect.MethodCallback> methods = methodCallbackAspect.getMethodCallbacks(name);

                //define method
                if (method == null) {
                    method = methodGenerator.buildMethod(definedClass);
                }
                JBlock body = method.body();

                for (MethodCallbackAspect.MethodCallback methodCallback : methods) {
                    //todo: non-public access
                    body.add(injectionNodeJExpressionEntry.getValue().invoke(methodCallback.getMethod().getName()));
                }
            }
        }

        methodGenerator.closeMethod(method);
    }
}
