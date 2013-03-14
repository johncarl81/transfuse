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

import android.app.Activity;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.targets.MockActivityDelegate;
import org.androidtransfuse.annotations.ScopeReference;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.config.ConfigurationScope;
import org.androidtransfuse.config.EnterableScope;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.manifest.Application;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.scope.ScopeKey;
import org.androidtransfuse.util.EmptyRResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
@Bootstrap
public class ActivityAnalysisTest {

    public static final String TEST_NAME = "ActivityTestTarget";
    public static final int TEST_LAYOUT_ID = 123456;

    @Inject
    private ActivityAnalysis activityAnalysis;
    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    @ScopeReference(ConfigurationScope.class)
    private EnterableScope configurationScope;

    @Before
    public void setup() {
        Bootstraps.inject(this);

        Manifest manifest = new Manifest();
        manifest.getApplications().add(new Application());

        configurationScope.seed(ScopeKey.of(RResource.class), new EmptyRResource());
        //todo: configurationScope.seed(Key.get(Manifest.class, Names.named(TransfuseAndroidModule.ORIGINAL_MANIFEST)), manifest);
        configurationScope.seed(ScopeKey.of(Manifest.class), manifest);
        configurationScope.enter();
    }

    @After
    public void tearDown() {
        configurationScope.exit();
    }

    @Test
    public void testActivityAnnotation() {
        ComponentDescriptor activityDescriptor = activityAnalysis.analyze(astClassFactory.getType(MockActivityDelegate.class));
        assertEquals(TEST_NAME, activityDescriptor.getPackageClass().getClassName());

        assertEquals(Activity.class.getName(), activityDescriptor.getType());

        //todo:fill in
    }
}
