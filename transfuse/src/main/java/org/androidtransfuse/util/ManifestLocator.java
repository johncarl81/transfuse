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
package org.androidtransfuse.util;

import org.androidtransfuse.analysis.TransfuseAnalysisException;

import javax.annotation.processing.Filer;
import javax.inject.Inject;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Copied respectfully from the AndroidAnnotations project:
 * com.googlecode.androidannotations.helper.AndroidManifestFinder
 *
 * Original Header:
 *
 * Copyright (C) 2010-2011 eBusiness Information, Excilys Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author John Ericksen
 */
public class ManifestLocator {

    private static final int MAX_PARENTS_FROM_SOURCE_FOLDER = 10;

    private final Filer filer;
    private final Logger logger;

    @Inject
    public ManifestLocator(Filer filer, Logger logger) {
        this.filer = filer;
        this.logger = logger;
    }

    public File findManifest() {
        try {
            return findManifestFileThrowing();
        } catch (URISyntaxException e) {
            logger.error("URISyntaxException while trying to load manifest", e);
            throw new TransfuseAnalysisException("URISyntaxException while trying to load manifest", e);
        } catch (IOException e) {
            logger.error("IOException while trying to load manifest", e);
            throw new TransfuseAnalysisException("IOException while trying to load manifest", e);
        }
    }

    /**
     * We use a dirty trick to find the AndroidManifest.xml file, since it's not
     * available in the classpath. The idea is quite simple : create a fake
     * class file, retrieve its URI, and start going up in parent folders to
     * find the AndroidManifest.xml file. Any better solution will be
     * appreciated.
     */
    private File findManifestFileThrowing() throws IOException, URISyntaxException {
        JavaFileObject dummySourceFile = filer.createSourceFile("dummy" + System.currentTimeMillis());
        String dummySourceFilePath = dummySourceFile.toUri().toString();

        if (dummySourceFilePath.startsWith("file:")) {
            if (!dummySourceFilePath.startsWith("file://")) {
                dummySourceFilePath = "file://" + dummySourceFilePath.substring("file:".length());
            }
        } else {
            dummySourceFilePath = "file://" + dummySourceFilePath;
        }


        logger.info("Dummy source file: " + dummySourceFilePath);

        URI cleanURI = new URI(dummySourceFilePath);

        File dummyFile = new File(cleanURI);

        File sourcesGenerationFolder = dummyFile.getParentFile();

        File projectRoot = sourcesGenerationFolder.getParentFile();

        File androidManifestFile = new File(projectRoot, "AndroidManifest.xml");
        for (int i = 0; i < MAX_PARENTS_FROM_SOURCE_FOLDER; i++) {
            if (androidManifestFile.exists()) {
                break;
            } else {
                if (projectRoot.getParentFile() != null) {
                    projectRoot = projectRoot.getParentFile();
                    androidManifestFile = new File(projectRoot, "AndroidManifest.xml");
                } else {
                    break;
                }
            }
        }

        if (!androidManifestFile.exists()) {
            throw new IllegalStateException("Could not find the AndroidManifest.xml file, going up from path [" + sourcesGenerationFolder.getAbsolutePath() + "] found using dummy file [" + dummySourceFilePath + "] (max atempts: " + MAX_PARENTS_FROM_SOURCE_FOLDER + ")");
        } else {
            logger.info("AndroidManifest.xml file found: " + androidManifestFile.toString());
        }

        return androidManifestFile;
    }

}
