package org.androidrobotics.model.manifest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.thoughtworks.xstream.XStream;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.util.EmptyProcessingEnvironment;
import org.androidrobotics.util.JavaUtilLogger;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.*;

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
    public void testManifestParsingAndWriting() {
        try {

            InputStream manifestStream = this.getClass().getClassLoader().getResourceAsStream("AndroidManifest.xml");
            String manifestString = IOUtils.toString(manifestStream);
            Manifest manifest = (Manifest) xStream.fromXML(manifestString);

            assertNotNull(manifest);
            assertEquals("android.permission.VIBRATE", manifest.getUsesPermissions().get(0).getName());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
            writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");

            xStream.toXML(manifest, writer);

            String output = new String(outputStream.toByteArray());

            assertEquals(formatWhitespace(manifestString), formatWhitespace(output));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatWhitespace(String input) {
        return input.replaceAll("\\s+", " ");
    }
}
