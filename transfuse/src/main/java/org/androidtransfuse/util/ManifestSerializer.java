/**
 * Copyright 2011-2015 John Ericksen
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

import org.androidtransfuse.model.manifest.Manifest;

import javax.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.*;

/**
 * Serializes the Manifest to and from xml
 *
 * @author John Ericksen
 */
public class ManifestSerializer {

    private final JAXBContext context;
    private final Logger log;

    @Inject
    public ManifestSerializer(JAXBContext context, Logger log) {
        this.context = context;
        this.log = log;
    }

    public Manifest readManifest(File manifestFile) {
        try{
            return (Manifest) context.createUnmarshaller().unmarshal(manifestFile);
        } catch (JAXBException e) {
            log.error("IOException while writing manifest", e);
            throw new TransfuseRuntimeException("IOException while writing manifest", e);
        }
    }

    public Manifest readManifest(InputStream manifestInputStream) {
        try{
            return (Manifest) context.createUnmarshaller().unmarshal(manifestInputStream);
        } catch (JAXBException e) {
            log.error("IOException while writing manifest", e);
            throw new TransfuseRuntimeException("IOException while writing manifest", e);
        }
    }

    public void writeManifest(Manifest manifest, OutputStream manifestStream) {
        try {
            Writer writer = new OutputStreamWriter(manifestStream, "UTF-8");

            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(manifest, writer);
        } catch (IOException e) {
            log.error("IOException while writing manifest", e);
            throw new TransfuseRuntimeException("IOException while writing manifest", e);
        } catch (JAXBException e) {
            log.error("JAXBException while writing manifest", e);
            throw new TransfuseRuntimeException("JAXBException while writing manifest", e);
        }
    }

    public void writeManifest(Manifest manifest, File manifestFile) {
        try {
            writeManifest(manifest, new FileOutputStream(manifestFile));
        } catch (IOException e) {
            log.error("IOException while writing manifest", e);
            throw new TransfuseInjectionException("IOException while writing manifest", e);
        }
    }
}
