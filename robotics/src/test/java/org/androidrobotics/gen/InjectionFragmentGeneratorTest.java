package org.androidrobotics.gen;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.gen.target.*;
import org.androidrobotics.model.*;
import org.androidrobotics.util.JavaUtilLogger;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class InjectionFragmentGeneratorTest {

    @Inject
    private InjectionFragmentGeneratorHarness fragementGeneratorHarness;
    @Inject
    private CodeGenerationUtil codeGenerationUtil;
    @Inject
    private JCodeModel codeModel;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new RoboticsGenerationGuiceModule(new JavaUtilLogger(this)));
        injector.injectMembers(this);
    }

    @Test
    public void testConstrictorInjection() throws Exception {
        InjectionNode injectionNode = buildInjectionNode(ConstructorInjectable.class);
        //reset constructor injection
        injectionNode.getConstructorInjectionPoints().clear();

        ConstructorInjectionPoint constructorInjectionPoint = new ConstructorInjectionPoint();
        constructorInjectionPoint.addInjectionNode(buildInjectionNode(InjectionTarget.class));
        injectionNode.addInjectionPoint(constructorInjectionPoint);

        ConstructorInjectable constructorInjectable = buildInstance(ConstructorInjectable.class, injectionNode);

        assertNotNull(constructorInjectable.getInjectionTarget());
    }

    @Test
    public void testMethodInjection() throws Exception {
        InjectionNode injectionNode = buildInjectionNode(MethodInjectable.class);

        MethodInjectionPoint methodInjectionPoint = new MethodInjectionPoint("setInjectionTarget");
        methodInjectionPoint.addInjectionNode(buildInjectionNode(InjectionTarget.class));
        injectionNode.addInjectionPoint(methodInjectionPoint);

        MethodInjectable methodInjectable = buildInstance(MethodInjectable.class, injectionNode);

        assertNotNull(methodInjectable.getInjectionTarget());
    }

    @Test
    public void testFieldInjection() throws Exception {
        InjectionNode injectionNode = buildInjectionNode(FieldInjectable.class);

        FieldInjectionPoint fieldInjectionPoint = new FieldInjectionPoint("injectionTarget", buildInjectionNode(InjectionTarget.class));
        injectionNode.addInjectionPoint(fieldInjectionPoint);

        FieldInjectable fieldInjectable = buildInstance(FieldInjectable.class, injectionNode);

        assertNotNull(fieldInjectable.getInjectionTarget());
    }

    @Test
    public void testVariableBuilder() throws Exception {
        InjectionNode injectionNode = buildInjectionNode(VariableBuilderInjectable.class);

        FieldInjectionPoint fieldInjectionPoint = new FieldInjectionPoint("target", buildInjectionNode(VariableTarget.class));
        injectionNode.addInjectionPoint(fieldInjectionPoint);

        Map<String, VariableBuilder> builderMap = new HashMap<String, VariableBuilder>();

        builderMap.put(VariableTarget.class.getName(), new VariableBuilder() {
            @Override
            public JExpression buildVariable() {
                return JExpr._new(codeModel.ref(VariableTarget.class));
            }
        });

        VariableBuilderInjectable vbInjectable = buildInstance(VariableBuilderInjectable.class, injectionNode, builderMap);

        assertNotNull(vbInjectable.getTarget());
    }

    private InjectionNode buildInjectionNode(Class<?> instanceClass) {
        PackageClass packageClass = new PackageClass(instanceClass);
        InjectionNode injectionNode = new InjectionNode(packageClass.getFullyQualifiedName());

        ConstructorInjectionPoint noArgConstructorInjectionPoint = new ConstructorInjectionPoint();
        injectionNode.addInjectionPoint(noArgConstructorInjectionPoint);

        return injectionNode;
    }

    private <T> T buildInstance(Class<T> instanceClass, InjectionNode injectionNode) throws Exception {
        return buildInstance(instanceClass, injectionNode, new HashMap<String, VariableBuilder>());
    }

    private <T> T buildInstance(Class<T> instanceClass, InjectionNode injectionNode, Map<String, VariableBuilder> builderMap) throws Exception {
        PackageClass providerPackageClass = new PackageClass(instanceClass).add("Provider");

        fragementGeneratorHarness.buildProvider(injectionNode, providerPackageClass, builderMap);

        ClassLoader classLoader = codeGenerationUtil.build(false);
        Class<Provider> generatedFactoryClass = (Class<Provider>) classLoader.loadClass(providerPackageClass.getFullyQualifiedName());

        assertNotNull(generatedFactoryClass);
        Provider provider = generatedFactoryClass.newInstance();
        Object result = provider.get();
        assertEquals(instanceClass, result.getClass());

        return (T) result;
    }
}
