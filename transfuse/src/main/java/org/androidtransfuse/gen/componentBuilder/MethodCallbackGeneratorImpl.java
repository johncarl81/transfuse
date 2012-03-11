package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
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
    private JCodeModel codeModel;

    @Inject
    public MethodCallbackGeneratorImpl(@Assisted String name, JCodeModel codeModel, @Assisted Class<?>... methodParameterTypes) {
        this.name = name;
        this.codeModel = codeModel;
        this.methodParameterTypes = new ArrayList<Class<?>>();

        if (methodParameterTypes != null) {
            this.methodParameterTypes.addAll(Arrays.asList(methodParameterTypes));
        }
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
                    method = definedClass.method(JMod.PUBLIC, codeModel.VOID, name);
                    JBlock body = method.body();
                    body.add(JExpr._super().invoke(name));
                }
                JBlock body = method.body();

                for (MethodCallbackAspect.MethodCallback methodCallback : methods) {
                    //todo: non-public access
                    body.add(injectionNodeJExpressionEntry.getValue().invoke(methodCallback.getMethod().getName()));
                }
            }
        }
    }
}
