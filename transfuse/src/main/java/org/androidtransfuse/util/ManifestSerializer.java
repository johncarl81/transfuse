package org.androidtransfuse.util;

import com.thoughtworks.xstream.XStream;
import org.androidtransfuse.model.manifest.Manifest;

import javax.inject.Inject;
import java.io.*;

/**
 * @author John Ericksen
 */
public class ManifestSerializer {

    private XStream xStream;
    private Logger logger;

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
            writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");

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
