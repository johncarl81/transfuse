package org.androidrobotics.gen;

import android.app.Activity;
import android.os.Bundle;
import com.google.inject.Injector;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.TestInjectorBuilder;
import org.androidrobotics.gen.classloader.MemoryClassLoader;
import org.androidrobotics.model.ActivityDescriptor;
import org.androidrobotics.model.PackageClass;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class ActivityGeneratorTest {

    private static final String TEST_PACKLAGE = "org.androidrobotics.gen";
    private static final String TEST_NAME = "MockActivity";
    private static final PackageClass TEST_PACKAGE_FILENAME = new PackageClass(TEST_PACKLAGE, TEST_NAME);
    private static final int TEST_LAYOUT = 1234;

    private ActivityGenerator activityGenerator;
    private JCodeModel codeModel;
    private MemoryClassLoader classLoader;
    private StringCodeWriter stringCodeWriter;

    @Before
    public void setUp() throws Exception {
        Injector injector = TestInjectorBuilder.createInjector(this);

        activityGenerator = injector.getInstance(ActivityGenerator.class);
        codeModel = injector.getInstance(JCodeModel.class);
        classLoader = injector.getInstance(MemoryClassLoader.class);
        stringCodeWriter = injector.getInstance(StringCodeWriter.class);
    }

    @Test
    public void testOnCreate() throws ClassNotFoundException, IOException, JClassAlreadyExistsException, NoSuchMethodException {

        ActivityDescriptor activityDescriptor = new ActivityDescriptor();
        activityDescriptor.setPackageClass(TEST_PACKAGE_FILENAME);
        activityDescriptor.setLayout(TEST_LAYOUT);

        activityGenerator.generate(activityDescriptor);

        codeModel.build(stringCodeWriter);
        classLoader.add(TEST_PACKAGE_FILENAME.getFullyQualifiedName(), stringCodeWriter.getValue(TEST_PACKAGE_FILENAME.addDotJava()));
        Class<?> generatedActivityClass = classLoader.loadClass(TEST_PACKAGE_FILENAME.getFullyQualifiedName());

        assertEquals(Activity.class, generatedActivityClass.getSuperclass());

        Method onCreateMethod = generatedActivityClass.getMethod("onCreate", Bundle.class);
        assertNotNull(onCreateMethod);
        assertEquals(Void.TYPE, onCreateMethod.getReturnType());
    }
}
