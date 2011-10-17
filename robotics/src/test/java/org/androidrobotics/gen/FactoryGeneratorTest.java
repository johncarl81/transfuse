package org.androidrobotics.gen;

import com.google.inject.Injector;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.TestInjectorBuilder;
import org.androidrobotics.gen.classloader.MemoryClassLoader;
import org.androidrobotics.gen.target.ConstructorInjectable;
import org.androidrobotics.gen.target.FieldInjectable;
import org.androidrobotics.gen.target.InjectionTarget;
import org.androidrobotics.gen.target.MethodInjectable;
import org.androidrobotics.model.*;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class FactoryGeneratorTest {

    private FactoryGenerator factoryGenerator;
    //private TestProvider provider;
    private MemoryClassLoader classLoader;
    private Map<Class, PackageClass> classFactories;
    private JCodeModel codeModel;
    private StringCodeWriter stringCodeWriter;

    @Before
    public void setUp() throws Exception {
        Injector injector = TestInjectorBuilder.createInjector(this);

        factoryGenerator = injector.getInstance(FactoryGenerator.class);
        codeModel = injector.getInstance(JCodeModel.class);
        classLoader = injector.getInstance(MemoryClassLoader.class);
        stringCodeWriter = injector.getInstance(StringCodeWriter.class);
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
        PackageClass factoryPackageClass = new PackageClass(instanceClass).add("Factory");

        FactoryDescriptor factoryDescriptor = factoryGenerator.buildFactory(injectionNode);

        codeModel.build(stringCodeWriter);

        System.out.println(stringCodeWriter.getValue(factoryPackageClass.addDotJava()));

        classLoader.add(stringCodeWriter.getOutput());
        Class<?> generatedFactoryClass = classLoader.loadClass(factoryPackageClass.getFullyQualifiedName());

        assertNotNull(generatedFactoryClass);
        Method getInstance = generatedFactoryClass.getMethod(factoryDescriptor.getInstanceMethodName());
        Method buildInstance = generatedFactoryClass.getMethod(factoryDescriptor.getBuilderMethodName());
        assertNotNull(getInstance);
        assertNotNull(buildInstance);
        Object factory = getInstance.invoke(generatedFactoryClass);
        Object result = buildInstance.invoke(factory);
        assertEquals(instanceClass, result.getClass());

        return (T) result;
    }
}
