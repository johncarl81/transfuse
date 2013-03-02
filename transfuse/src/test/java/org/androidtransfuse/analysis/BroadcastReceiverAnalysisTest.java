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
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.model.ComponentDescriptor;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Bootstrap
public class BroadcastReceiverAnalysisTest {

    @Inject
    private BroadcastReceiverAnalysis analysis;
    @Inject
    private ASTClassFactory astClassFactory;
    private ASTType broadcastReceiverType;

    @BroadcastReceiver
    public class BroadcastReceiverTarget {
    }

    @Before
    public void setup() {
        Bootstraps.inject(this);

        broadcastReceiverType = astClassFactory.getType(BroadcastReceiverTarget.class);
    }

    @Test
    public void testAnalysis() {
        ComponentDescriptor analyze = analysis.analyze(broadcastReceiverType);

        //todo:fill in
    }

}
