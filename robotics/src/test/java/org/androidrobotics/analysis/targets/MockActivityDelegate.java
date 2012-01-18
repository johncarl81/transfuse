package org.androidrobotics.analysis.targets;

import org.androidrobotics.analysis.ActivityAnalysisTest;
import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Layout;
import org.androidrobotics.annotations.OnCreate;

@Activity(name = ActivityAnalysisTest.TEST_NAME)
@Layout(ActivityAnalysisTest.TEST_LAYOUT_ID)
public class MockActivityDelegate {

    @OnCreate
    public void onCreate() {

    }
}