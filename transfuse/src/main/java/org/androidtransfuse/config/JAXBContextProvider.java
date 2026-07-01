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
package org.androidtransfuse.config;

import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.manifest.UsesPermission;
import org.androidtransfuse.util.TransfuseRuntimeException;
import com.sun.xml.bind.v2.ContextFactory;

import javax.inject.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * @author John Ericksen
 */
public class JAXBContextProvider implements Provider<JAXBContext> {

    public JAXBContext get(){
        try {
            // Prefer the concrete JAXB RI factory to avoid relying on service lookups
            return ContextFactory.createContext(new Class[]{Manifest.class, UsesPermission.class}, null);
        } catch (JAXBException e) {
            throw new TransfuseRuntimeException("Unable to create JAXBContext", e);
        }
    }
}
