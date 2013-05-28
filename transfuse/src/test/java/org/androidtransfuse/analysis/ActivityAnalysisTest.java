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
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.model.ComponentDescriptor;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

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

    @Before
    public void setup() {
        Bootstraps.inject(this);
    }

    @Test
    public void testActivityAnnotation() {
        ComponentDescriptor activityDescriptor = activityAnalysis.analyze(astClassFactory.getType(MockActivityDelegate.class));
        assertEquals(TEST_NAME, activityDescriptor.getPackageClass().getClassName());

        assertEquals(Activity.class.getName(), activityDescriptor.getType());

        assertEquals(MockActivityDelegate.class.getPackage().getName(), activityDescriptor.getPackageClass().getPackage());
    }

    @Test
    public void testManifestEntry(){
        @org.androidtransfuse.annotations.Activity(label = "label", process = "process", icon = "icon", permission = "permission", exported = Exported.FALSE,
                allowTaskReparenting = true, alwaysRetainTaskState = true, clearTaskOnLaunch = true, configChanges = {ConfigChanges.NAVIGATION, ConfigChanges.KEYBOARD}, enabled = false,
                excludeFromRecents = true, finishOnTaskLaunch = true, hardwareAccelerated = true, launchMode = LaunchMode.SINGLE_INSTANCE, multiprocess = true,
                noHistory = true, screenOrientation = ScreenOrientation.LANDSCAPE, stateNotNeeded = true, taskAffinity = "taskAffinity", theme = "theme",
                uiOptions = UIOptions.SPLIT_ACTION_BAR_WHEN_NARROW, windowSoftInputMode = WindowSoftInputMode.ADJUST_RESIZE)
        class ActivityAnalysisTarget {}

        org.androidtransfuse.model.manifest.Activity activity = activityAnalysis.buildManifestEntry("name", ActivityAnalysisTarget.class.getAnnotation(org.androidtransfuse.annotations.Activity.class));

        assertEquals("name", activity.getName());
        assertEquals("label", activity.getLabel());
        assertEquals("process", activity.getProcess());
        assertEquals("icon", activity.getIcon());
        assertEquals("permission", activity.getPermission());
        assertEquals(false, activity.getExported());
        assertEquals(true, activity.getAllowTaskReparenting());
        assertEquals(true, activity.getAlwaysRetainTaskState());
        assertEquals(true, activity.getClearTaskOnLaunch());
        assertEquals("navigation|keyboard", activity.getConfigChanges());
        assertEquals(false, activity.getEnabled());
        assertEquals(true, activity.getExcludeFromRecents());
        assertEquals(true, activity.getFinishOnTaskLaunch());
        assertEquals(true, activity.getHardwareAccelerated());
        assertEquals(LaunchMode.SINGLE_INSTANCE, activity.getLaunchMode());
        assertEquals(true, activity.getMultiprocess());
        assertEquals(true, activity.getNoHistory());
        assertEquals(ScreenOrientation.LANDSCAPE, activity.getScreenOrientation());
        assertEquals(true, activity.getStateNotNeeded());
        assertEquals("taskAffinity", activity.getTaskAffinity());
        assertEquals("theme", activity.getTheme());
        assertEquals(UIOptions.SPLIT_ACTION_BAR_WHEN_NARROW, activity.getUiOptions());
        assertEquals(WindowSoftInputMode.ADJUST_RESIZE, activity.getWindowSoftInputMode());
    }
}
