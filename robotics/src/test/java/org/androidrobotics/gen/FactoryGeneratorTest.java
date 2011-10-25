package org.androidrobotics.gen;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.gen.classloader.MemoryClassLoader;
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
import java.lang.reflect.Method;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class FactoryGeneratorTest {

    @Inject
    private FactoryGenerator factoryGenerator;
    @Inject
    private MemoryClassLoader classLoader;
    @Inject
    private JCodeModel codeModel;
    @Inject
    private StringCodeWriter stringCodeWriter;
    private Map<Class, PackageClass> classFactories;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new RoboticsGenerationGuiceModule(new JavaUtilLogger(this)));
        injector.injectMembers(this);
    }

    @Test
    public void testContructorInjection() throws Exception {
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
        PackageClass providerPackageClass = new PackageClass(instanceClass).add("Factory");

        FactoryDescriptor factoryDescriptor = factoryGenerator.buildFactory(injectionNode);

        codeModel.build(stringCodeWriter);

        classLoader.add(stringCodeWriter.getOutput());
        Class<?> generatedFactoryClass = classLoader.loadClass(providerPackageClass.getFullyQualifiedName());

        assertNotNull(generatedFactoryClass);
        Method getInstance = generatedFactoryClass.getMethod(factoryDescriptor.getInstanceMethodName());
        Method buildInstance = generatedFactoryClass.getMethod(factoryDescriptor.getBuilderMethodName());
        assertNotNull(getInstance);
        assertNotNull(buildInstance);
        Provider provider = (Provider) getInstance.invoke(generatedFactoryClass);
        Object result = provider.get();
        assertEquals(instanceClass, result.getClass());

        return (T) result;
    }
}
