package org.androidtransfuse.model.manifest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.androidtransfuse.config.TransfuseGenerationGuiceModule;
import org.androidtransfuse.util.JavaUtilLogger;
import org.androidtransfuse.util.ManifestSerializer;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class ManifestTest {

    @Inject
    private ManifestSerializer manifestSerializer;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new TransfuseGenerationGuiceModule(new JavaUtilLogger(this)));
        injector.injectMembers(this);
    }

    @Test
    public void testManifestParsingAndWriting() {
        try {

            InputStream manifestStream = this.getClass().getClassLoader().getResourceAsStream("AndroidManifest.xml");
            String manifestString = IOUtils.toString(manifestStream);
            Manifest manifest = manifestSerializer.readManifest(new ByteArrayInputStream(manifestString.getBytes()));

            assertNotNull(manifest);
            assertEquals("android.permission.VIBRATE", manifest.getUsesPermissions().get(0).getName());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            manifestSerializer.writeManifest(manifest, outputStream);

            String output = IOUtils.toString(new ByteArrayInputStream(outputStream.toByteArray()));

            assertEquals(formatWhitespace(manifestString), formatWhitespace(output));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatWhitespace(String input) {
        return input.replaceAll("\\s+", " ");
    }
}
