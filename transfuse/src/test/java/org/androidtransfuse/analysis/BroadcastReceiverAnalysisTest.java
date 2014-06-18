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
import org.androidtransfuse.annotations.BroadcastReceiver;
import org.androidtransfuse.annotations.Exported;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.manifest.Receiver;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * @author John Ericksen
 */
@Bootstrap
public class BroadcastReceiverAnalysisTest {

    @Inject
    private BroadcastReceiverAnalysis analyzer;
    @Inject
    private ASTClassFactory astClassFactory;

    @Before
    public void setup() {
        Bootstraps.inject(this);
    }

    @Test
    public void testAnalysis() {
        @BroadcastReceiver
        class BroadcastReceiverTarget {
        }

        ASTType broadcastReceiverType = astClassFactory.getType(BroadcastReceiverTarget.class);
        ComponentDescriptor analysis = analyzer.analyze(broadcastReceiverType);

        assertEquals(android.content.BroadcastReceiver.class.getName(), analysis.getType());
        assertEquals("org.androidtransfuse.analysis", analysis.getPackageClass().getPackage());
        assertEquals("BroadcastReceiverAnalysisTest$1BroadcastReceiverTargetBroadcastReceiver", analysis.getPackageClass().getClassName());
    }

    @Test
    public void testManifestEntry(){

        @BroadcastReceiver(label = "label", permission = "permission", process = "process", icon = "icon", enabled = false, exported = Exported.FALSE)
        class BroadcastReceiverTarget {
        }

        Receiver receiver = analyzer.buildReceiver("name", BroadcastReceiverTarget.class.getAnnotation(BroadcastReceiver.class));

        assertEquals("name", receiver.getName());
        assertEquals("label", receiver.getLabel());
        assertEquals("permission", receiver.getPermission());
        assertEquals("process", receiver.getProcess());
        assertEquals("icon", receiver.getIcon());
        assertEquals(false, receiver.getEnabled()); //false is the non-default case
        assertEquals(false, receiver.getExported()); //false is the non-default case
    }

}
