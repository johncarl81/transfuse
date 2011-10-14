package org.androidrobotics.gen;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.gen.classloader.MemoryClassLoader;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class FactoryGeneratorTest {

    private static final String TEST_PROPERTY = "Test property";
    private static final int TEST_VALUE = 42;

    //private TestProvider provider;
    private MemoryClassLoader classLoader;
    private Map<Class, PackageFileName> classFactories;

    @Before
    public void setup() throws ClassNotFoundException, JClassAlreadyExistsException, IOException {
        Injector injector = Guice.createInjector(new RoboticsGenerationGuiceModule());
        FactoryGenerator factoryGeneratorTwo = injector.getInstance(FactoryGenerator.class);
        JCodeModel codeModel = injector.getInstance(JCodeModel.class);

        //factoryGeneratorTwo.buildProvider(new ClassAnalysisBridge(ConstructorProvided.class));
        //factoryGeneratorTwo.buildProvider(new ClassAnalysisBridge(SetterProvided.class));
        //factoryGeneratorTwo.buildProvider(new ClassAnalysisBridge(ParameterProvided.class));

        StringCodeWriter codeWriter = new StringCodeWriter();

        //write out the built code
        codeModel.build(codeWriter);

        //build classes
        Map<String, String> factoryClassContentMap = new HashMap<String, String>();
        classFactories = buildClassFactoryNames(new Class<?>[]{ConstructorProvided.class, SetterProvided.class, ParameterProvided.class});
        for (Map.Entry<Class, PackageFileName> factoryMapEntry : classFactories.entrySet()) {
            String factoryClassContents = codeWriter.getValue(factoryMapEntry.getValue().addDotJava());

            System.out.println(factoryClassContents);

            factoryClassContentMap.put(factoryMapEntry.getValue().getName(), factoryClassContents);
        }

        //setup class loader
        classLoader = new MemoryClassLoader(factoryClassContentMap);

    }

    private Map<Class, PackageFileName> buildClassFactoryNames(Class<?>[] classes) {
        Map<Class, PackageFileName> classFactoryMap = new HashMap<Class, PackageFileName>();

        for (Class clazz : classes) {
            classFactoryMap.put(clazz,
                    new PackageFileName(
                            clazz.getPackage().getName(),
                            clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1) + "Factory"
                    ));
        }

        return classFactoryMap;
    }

    @Test
    public void testContructorInjection() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<Provider> factoryClass = (Class<Provider>) classLoader.loadClass(classFactories.get(ConstructorProvided.class).toString());
        Provider<ConstructorProvided> factoryInstance = factoryClass.newInstance();

        ConstructorProvided provided = factoryInstance.get();

        provided.validate();
    }

    @Test
    public void testSetterInjection() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<Provider> factoryClass = (Class<Provider>) classLoader.loadClass(classFactories.get(SetterProvided.class).toString());
        Provider<SetterProvided> factoryInstance = factoryClass.newInstance();

        SetterProvided provided = factoryInstance.get();

        provided.validate();
    }

    @Test
    public void testParameterInjection() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<Provider> factoryClass = (Class<Provider>) classLoader.loadClass(classFactories.get(ParameterProvided.class).toString());
        Provider<ParameterProvided> factoryInstance = factoryClass.newInstance();

        ParameterProvided provided = factoryInstance.get();

        provided.validate();
    }

    public static class ConstructorProvided {

        String property;
        int value;

        @Inject
        public ConstructorProvided(String property, int value) {
            this.property = property;
            this.value = value;
        }

        public void validate() {
            assertEquals(TEST_PROPERTY, property);
            assertEquals(TEST_VALUE, value);
        }
    }

    public static class SetterProvided {

        String property;
        int value;

        @Inject
        public void setProperty(String property) {
            this.property = property;
        }

        @Inject
        public void setValue(int value) {
            this.value = value;
        }

        public void validate() {
            assertEquals(TEST_PROPERTY, property);
            assertEquals(TEST_VALUE, value);
        }
    }

    public static class ParameterProvided {
        @Inject
        String property;
        @Inject
        int value;

        public void validate() {
            assertEquals(TEST_PROPERTY, property);
            assertEquals(TEST_VALUE, value);
        }
    }
}
