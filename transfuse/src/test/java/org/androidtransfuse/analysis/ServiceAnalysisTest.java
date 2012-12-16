/**
 * Copyright 2012 John Ericksen
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

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Service;
import org.androidtransfuse.model.ComponentDescriptor;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ServiceAnalysisTest {

    @Inject
    private ServiceAnalysis serviceAnalysis;
    @Inject
    private ASTClassFactory astClassFactory;
    private ASTType serviceTargetType;

    @Service
    public class ServiceAnalysisTarget {
    }

    @Before
    public void setup() {
        TransfuseTestInjector.inject(this);

        serviceTargetType = astClassFactory.getType(ServiceAnalysisTarget.class);
    }

    @Test
    public void testAnalysis() {
        ComponentDescriptor componentDescriptor = serviceAnalysis.analyze(serviceTargetType);

        //todo:fill in
    }

}
