package org.androidrobotics.gen;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.gen.target.ConstructorInjectable;
import org.androidrobotics.gen.target.FieldInjectable;
import org.androidrobotics.gen.target.InjectionTarget;
import org.androidrobotics.gen.target.MethodInjectable;
import org.androidrobotics.model.*;
import org.androidrobotics.util.JavaUtilLogger;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;

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

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new RoboticsGenerationGuiceModule(new JavaUtilLogger(this)));
        injector.injectMembers(this);
    }

    @Test
    public void testConstrictorInjection() throws Exception {
        InjectionNode injectionNode = buildInjectionNode(ConstructorInjectable.class);
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

    private InjectionNode buildInjectionNode(Class<?> instanceClass) {
        PackageClass packageClass = new PackageClass(instanceClass);
        InjectionNode injectionNode = new InjectionNode(packageClass.getFullyQualifiedName());

        ConstructorInjectionPoint noArgConstructorInjectionPoint = new ConstructorInjectionPoint();
        injectionNode.addInjectionPoint(noArgConstructorInjectionPoint);

        return injectionNode;
    }

    private <T> T buildInstance(Class<T> instanceClass, InjectionNode injectionNode) throws Exception {
        PackageClass providerPackageClass = new PackageClass(instanceClass).add("Provider");

        fragementGeneratorHarness.buildProvider(injectionNode, providerPackageClass);

        ClassLoader classLoader = codeGenerationUtil.build(false);
        Class<Provider> generatedFactoryClass = (Class<Provider>) classLoader.loadClass(providerPackageClass.getFullyQualifiedName());

        assertNotNull(generatedFactoryClass);
        Provider provider = generatedFactoryClass.newInstance();
        Object result = provider.get();
        assertEquals(instanceClass, result.getClass());

        return (T) result;
    }
}
