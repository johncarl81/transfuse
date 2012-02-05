package org.androidtransfuse.analysis.targets;

import org.androidtransfuse.analysis.ActivityAnalysisTest;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnCreate;

@Activity(name = ActivityAnalysisTest.TEST_NAME)
@Layout(ActivityAnalysisTest.TEST_LAYOUT_ID)
public class MockActivityDelegate {

    @OnCreate
    public void onCreate() {

    }
}