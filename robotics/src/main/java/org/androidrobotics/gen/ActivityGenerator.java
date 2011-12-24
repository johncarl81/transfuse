package org.androidrobotics.gen;

import android.os.Bundle;
import android.view.MotionEvent;
import com.sun.codemodel.*;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.astAnalyzer.MethodCallbackAspect;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
@Singleton
public class ActivityGenerator {

    private JCodeModel codeModel;
    private InjectionFragmentGenerator injectionFragmentGenerator;

    @Inject
    public ActivityGenerator(JCodeModel codeModel, InjectionFragmentGenerator injectionFragmentGenerator) {
        this.codeModel = codeModel;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
    }

    public void generate(ActivityDescriptor descriptor) throws IOException, JClassAlreadyExistsException, ClassNotFoundException {

        final JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, descriptor.getPackageClass().getFullyQualifiedName(), ClassType.CLASS);

        definedClass._extends(android.app.Activity.class);


        final JMethod onCreateMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, "onCreate");
        JVar savedInstanceState = onCreateMethod.param(Bundle.class, "savedInstanceState");

        JBlock block = onCreateMethod.body();

        //super call with saved instance state
        block.invoke(JExpr._super(), onCreateMethod).arg(savedInstanceState);

        //layout setting
        block.invoke("setContentView").arg(JExpr.lit(descriptor.getLayout()));

        //injector and injection points
        //todo: more than one?
        if (descriptor.getInjectionPoints().size() > 0) {
            FieldInjectionPoint fieldInjectionPoint = descriptor.getInjectionPoints().get(0);

            Map<InjectionNode, JExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, fieldInjectionPoint.getInjectionNode());


            //ontouch method
            addMethodCallbacks("onTouch", expressionMap, new MethodGenerator() {
                JMethod onTouchEventMethod;

                @Override
                public JMethod buildMethod() {
                    onTouchEventMethod = definedClass.method(JMod.PUBLIC, codeModel.BOOLEAN, "onTouchEvent");
                    onTouchEventMethod.param(MotionEvent.class, "motionEvent");
                    return onTouchEventMethod;
                }

                @Override
                public void closeMethod() {
                    onTouchEventMethod.body()._return(JExpr.TRUE);
                }
            });

            // onCreate
            addMethodCallbacks("onCreate", expressionMap, new AlreadyDefinedMethodGenerator(onCreateMethod));
            // onDestroy
            addLifecycleMethod("onDestroy", definedClass, expressionMap);
            // onPause
            addLifecycleMethod("onPause", definedClass, expressionMap);
            // onRestart
            addLifecycleMethod("onRestart", definedClass, expressionMap);
            // onResume
            addLifecycleMethod("onResume", definedClass, expressionMap);
            // onStart
            addLifecycleMethod("onStart", definedClass, expressionMap);
            // onStop
            addLifecycleMethod("onStop", definedClass, expressionMap);
        }
    }

    private void addLifecycleMethod(String name, JDefinedClass definedClass, Map<InjectionNode, JExpression> expressionMap) {
        addMethodCallbacks(name, expressionMap, new SimpleMethodGenerator(name, definedClass));
    }

    private void addMethodCallbacks(String name, Map<InjectionNode, JExpression> expressionMap, MethodGenerator lazyMethodGenerator) {
        JMethod method = null;
        for (Map.Entry<InjectionNode, JExpression> injectionNodeJExpressionEntry : expressionMap.entrySet()) {
            MethodCallbackAspect methodCallbackAspect = injectionNodeJExpressionEntry.getKey().getAspect(MethodCallbackAspect.class);

            if (methodCallbackAspect != null) {
                Set<ASTMethod> methods = methodCallbackAspect.getMethods(name);

                if (methods != null) {

                    //define method
                    if (method == null) {
                        method = lazyMethodGenerator.buildMethod();
                    }
                    JBlock body = method.body();

                    for (ASTMethod astMethod : methodCallbackAspect.getMethods(name)) {
                        body.add(injectionNodeJExpressionEntry.getValue().invoke(astMethod.getName()));
                    }
                }
            }
        }
        if (method != null) {
            lazyMethodGenerator.closeMethod();
        }
    }

    private interface MethodGenerator {
        JMethod buildMethod();

        void closeMethod();
    }

    private class SimpleMethodGenerator implements MethodGenerator {
        private String name;
        private JDefinedClass definedClass;

        public SimpleMethodGenerator(String name, JDefinedClass definedClass) {
            this.name = name;
            this.definedClass = definedClass;
        }

        @Override
        public JMethod buildMethod() {
            JMethod method = definedClass.method(JMod.PUBLIC, codeModel.VOID, name);
            JBlock body = method.body();
            body.add(JExpr._super().invoke(name));
            return method;
        }

        @Override
        public void closeMethod() {
            //noop
        }
    }

    private class AlreadyDefinedMethodGenerator implements MethodGenerator {

        private JMethod method;

        private AlreadyDefinedMethodGenerator(JMethod method) {
            this.method = method;
        }

        @Override
        public JMethod buildMethod() {
            return method;
        }

        @Override
        public void closeMethod() {
            //noop
        }
    }
}
