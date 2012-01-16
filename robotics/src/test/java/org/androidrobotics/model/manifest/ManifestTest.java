package org.androidrobotics.model.manifest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.thoughtworks.xstream.XStream;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.util.EmptyProcessingEnvironment;
import org.androidrobotics.util.JavaUtilLogger;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class ManifestTest {

    @Inject
    private XStream xStream;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new RoboticsGenerationGuiceModule(new JavaUtilLogger(this), new EmptyProcessingEnvironment()));
        injector.injectMembers(this);
    }

    @Test
    public void test() {
        InputStream manifestStream = this.getClass().getClassLoader().getResourceAsStream("AndroidManifest.xml");
        Manifest manifest = (Manifest) xStream.fromXML(manifestStream);

        assertNotNull(manifest);
        assertEquals("android.permission.VIBRATE", manifest.getUsesPermissions().get(0).getName());
    }
}
