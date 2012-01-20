package org.androidrobotics.util;

import org.androidrobotics.analysis.RoboticsAnalysisException;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author John Ericksen
 */
public class ManifestLocator {

    private static final int MAX_PARENTS_FROM_SOURCE_FOLDER = 10;

    private Filer filer;
    private Logger logger;

    public ManifestLocator(Filer filer, Logger logger) {
        this.filer = filer;
        this.logger = logger;
    }

    public File findManifest() {
        try {
            return findManifestFileThrowing();
        } catch (URISyntaxException e) {
            logger.error("URISyntaxException while trying to load manifest", e);
            throw new RoboticsAnalysisException("URISyntaxException while trying to load manifest", e);
        } catch (IOException e) {
            logger.error("IOException while trying to load manifest", e);
            throw new RoboticsAnalysisException("IOException while trying to load manifest", e);
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
