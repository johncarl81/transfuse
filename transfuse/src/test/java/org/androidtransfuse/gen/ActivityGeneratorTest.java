package org.androidtransfuse.gen;

import android.app.Activity;
import android.os.Bundle;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.config.TransfuseGenerationGuiceModule;
import org.androidtransfuse.gen.classloader.MemoryClassLoader;
import org.androidtransfuse.model.ActivityDescriptor;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.model.r.RBuilder;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.util.JavaUtilLogger;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class ActivityGeneratorTest {

    private static final String TEST_PACKLAGE = "org.androidtransfuse.gen";
    private static final String TEST_NAME = "MockActivity";
    private static final PackageClass TEST_PACKAGE_FILENAME = new PackageClass(TEST_PACKLAGE, TEST_NAME);

    public static final class Layout {
        public static int value = 1234;
    }

    @Inject
    private ActivityGenerator activityGenerator;
    @Inject
    private JCodeModel codeModel;
    @Inject
    private MemoryClassLoader classLoader;
    @Inject
    private StringCodeWriter stringCodeWriter;
    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private RBuilder rBuilder;
    private RResource rResource;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new TransfuseGenerationGuiceModule(new JavaUtilLogger(this)));
        injector.injectMembers(this);

        rResource = rBuilder.buildR(Collections.singleton(astClassFactory.buildASTClassType(Layout.class)));
    }

    @Test
    public void testOnCreate() throws ClassNotFoundException, IOException, JClassAlreadyExistsException, NoSuchMethodException {

        ActivityDescriptor activityDescriptor = new ActivityDescriptor();
        activityDescriptor.setPackageClass(TEST_PACKAGE_FILENAME);
        activityDescriptor.setLayout(Layout.value);

        activityGenerator.generate(activityDescriptor, rResource);

        codeModel.build(stringCodeWriter);
        classLoader.add(TEST_PACKAGE_FILENAME.getFullyQualifiedName(), stringCodeWriter.getValue(TEST_PACKAGE_FILENAME.addDotJava()));
        Class<?> generatedActivityClass = classLoader.loadClass(TEST_PACKAGE_FILENAME.getFullyQualifiedName());

        assertEquals(Activity.class, generatedActivityClass.getSuperclass());

        Method onCreateMethod = generatedActivityClass.getMethod("onCreate", Bundle.class);
        assertNotNull(onCreateMethod);
        assertEquals(Void.TYPE, onCreateMethod.getReturnType());
    }
}
