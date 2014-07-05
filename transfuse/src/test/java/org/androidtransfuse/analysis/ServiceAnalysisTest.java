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
package org.androidtransfuse.analysis;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.annotations.Exported;
import org.androidtransfuse.annotations.Service;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Bootstrap
public class ServiceAnalysisTest {

    @Inject
    private ServiceAnalysis serviceAnalysis;
    @Inject
    private ASTClassFactory astClassFactory;

    @Before
    public void setup() {
        Bootstraps.inject(this);
    }

    @Test
    @Ignore
    public void testAnalysis() {
        @Service
        class ServiceAnalysisTarget {}

        ASTType serviceTargetType = astClassFactory.getType(ServiceAnalysisTarget.class);

        /*ComponentDescriptor componentDescriptor = serviceAnalysis.analyze(serviceTargetType);

        assertEquals(android.app.Service.class.getName(), componentDescriptor.getType());
        assertEquals("org.androidtransfuse.analysis", componentDescriptor.getPackageClass().getPackage());
        assertEquals("ServiceAnalysisTest$1ServiceAnalysisTargetService", componentDescriptor.getPackageClass().getClassName());*/
    }

    @Test
    public void testManifestEntry(){
        @Service(enabled = false, exported = Exported.FALSE, icon = "icon", label = "label", permission = "permission", process = "process")
        class ServiceAnalysisTarget{}

        /*org.androidtransfuse.model.manifest.Service service = serviceAnalysis.buildService("name", ServiceAnalysisTarget.class.getAnnotation(Service.class));

        assertEquals("name", service.getName());
        assertEquals("label", service.getLabel());
        assertEquals("permission", service.getPermission());
        assertEquals("process", service.getProcess());
        assertEquals("icon", service.getIcon());
        assertEquals(false, service.getEnabled()); //false is the non-default case
        assertEquals(false, service.getExported()); //false is the non-default case*/
    }
}
