/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.model.manifest;

import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.util.ManifestSerializer;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@Bootstrap
public class ManifestTest {

    @Inject
    private ManifestSerializer manifestSerializer;

    @Before
    public void setUp() {
        Bootstraps.inject(this);
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
