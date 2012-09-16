package org.androidtransfuse.util;

import com.thoughtworks.xstream.XStream;
import org.androidtransfuse.model.manifest.Manifest;

import javax.inject.Inject;
import java.io.*;

/**
 * Serializes the Manifest to and from xml
 *
 * @author John Ericksen
 */
public class ManifestSerializer {

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";

    private final XStream xStream;
    private final Logger logger;

    @Inject
    public ManifestSerializer(XStream xStream, Logger logger) {
        this.xStream = xStream;
        this.logger = logger;
    }

    public Manifest readManifest(File manifestFile) {
        return (Manifest) xStream.fromXML(manifestFile);
    }

    public Manifest readManifest(InputStream manifestInputStream) {
        return (Manifest) xStream.fromXML(manifestInputStream);
    }

    public void writeManifest(Manifest manifest, OutputStream manifestStream) {
        try {
            Writer writer = new OutputStreamWriter(manifestStream, "UTF-8");
            writer.write(XML_HEADER);

            xStream.toXML(manifest, writer);
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException while writing manifest", e);
            throw new TransfuseInjectionException(e);
        } catch (IOException e) {
            logger.error("IOException while writing manifest", e);
            throw new TransfuseInjectionException(e);
        }
    }

    public void writeManifest(Manifest manifest, File manifestFile) {
        try {
            writeManifest(manifest, new FileOutputStream(manifestFile));
        } catch (IOException e) {
            logger.error("IOException while writing manifest", e);
            throw new TransfuseInjectionException(e);
        }
    }
}
