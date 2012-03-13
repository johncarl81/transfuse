package org.androidtransfuse.gen;

import android.app.Application;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.astAnalyzer.MethodCallbackAspect;
import org.androidtransfuse.model.ApplicationDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.scope.Scope;
import org.androidtransfuse.scope.SingletonScope;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ApplicationGenerator {

    private JCodeModel codeModel;
    private InjectionFragmentGenerator injectionFragmentGenerator;
    private GeneratedClassAnnotator generatedClassAnnotator;

    @Inject
    public ApplicationGenerator(JCodeModel codeModel, InjectionFragmentGenerator injectionFragmentGenerator, GeneratedClassAnnotator generatedClassAnnotator) {
        this.codeModel = codeModel;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.generatedClassAnnotator = generatedClassAnnotator;
    }

    public void generate(ApplicationDescriptor descriptor, RResource rResource) throws JClassAlreadyExistsException, IOException, ClassNotFoundException {

        final JDefinedClass definedClass = codeModel._class(JMod.PUBLIC, descriptor.getPackageClass().getFullyQualifiedName(), ClassType.CLASS);

        generatedClassAnnotator.annotateClass(definedClass);

        definedClass._extends(Application.class);
        definedClass._implements(Scope.class);

        if (!descriptor.getInjectionNodes().isEmpty()) {

            final JMethod onCreateMethod = definedClass.method(JMod.PUBLIC, codeModel.VOID, "onCreate");
            JBlock block = onCreateMethod.body();
            block.invoke(JExpr._super(), onCreateMethod);

            for (InjectionNode injectionNode : descriptor.getInjectionNodes()) {


                Map<InjectionNode, JExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, injectionNode, rResource);

                addMethodCallbacks("onCreate", expressionMap, new AlreadyDefinedMethodGenerator(onCreateMethod));
                // onLowMemory
                addLifecycleMethod("onLowMemory", definedClass, expressionMap);
                // onTerminate
                addLifecycleMethod("onTerminate", definedClass, expressionMap);
            }
        }

        JClass singletonScopeClassRef = codeModel.ref(SingletonScope.class);
        JFieldVar singletonScopeField = definedClass.field(JMod.PRIVATE, singletonScopeClassRef, "singletonScope");
        singletonScopeField.init(JExpr._new(singletonScopeClassRef));

        //<T> T getScopedObject(Class<T> clazz, Provider<T> provider);
        JType genericT = codeModel.ref(Object.class);
        JClass clazzRef = codeModel.ref(Class.class);
        JClass providerRef = codeModel.ref(Provider.class);

        JMethod getScopedObjectMethod = definedClass.method(JMod.PUBLIC, genericT, "getScopedObject");
        JVar clazzParam = getScopedObjectMethod.param(clazzRef, "clazz");
        JVar providerParam = getScopedObjectMethod.param(providerRef, "provider");

        JBlock scopedObjectBody = getScopedObjectMethod.body();

        scopedObjectBody._return(singletonScopeField.invoke("getScopedObject").arg(clazzParam).arg(providerParam));
    }

    JType naiveType(String name) {
        try {
            return codeModel.parseType(name);
        } catch (ClassNotFoundException e) {
            return codeModel.directClass(name);
        }
    }

    private void addLifecycleMethod(String name, JDefinedClass definedClass, Map<InjectionNode, JExpression> expressionMap) {
        addMethodCallbacks(name, expressionMap, new SimpleMethodGenerator(name, definedClass, codeModel));
    }

    private void addMethodCallbacks(String name, Map<InjectionNode, JExpression> expressionMap, MethodGenerator lazyMethodGenerator) {
        JMethod method = null;
        for (Map.Entry<InjectionNode, JExpression> injectionNodeJExpressionEntry : expressionMap.entrySet()) {
            MethodCallbackAspect methodCallbackAspect = injectionNodeJExpressionEntry.getKey().getAspect(MethodCallbackAspect.class);

            if (methodCallbackAspect != null) {
                Set<MethodCallbackAspect.MethodCallback> methods = methodCallbackAspect.getMethodCallbacks(name);

                if (methods != null) {

                    //define method
                    if (method == null) {
                        method = lazyMethodGenerator.buildMethod();
                    }
                    JBlock body = method.body();

                    for (MethodCallbackAspect.MethodCallback methodCallback : methodCallbackAspect.getMethodCallbacks(name)) {
                        //todo: non-public access
                        body.add(injectionNodeJExpressionEntry.getValue().invoke(methodCallback.getMethod().getName()));
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

    private static final class SimpleMethodGenerator implements MethodGenerator {
        private String name;
        private JDefinedClass definedClass;
        private JCodeModel codeModel;

        public SimpleMethodGenerator(String name, JDefinedClass definedClass, JCodeModel codeModel) {
            this.name = name;
            this.definedClass = definedClass;
            this.codeModel = codeModel;
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

    private static final class AlreadyDefinedMethodGenerator implements MethodGenerator {

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