package org.androidtransfuse.analysis.targets;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class MockAnalysisClass extends MockAnalysisSuperClass {

    private MockParameter testParameter;

    @Inject
    public MockAnalysisClass(MockParameter testParameter) {
        this.testParameter = testParameter;
    }

    @Inject
    public void injectMethod(MockParameter testParameter) {
        this.testParameter = testParameter;
    }
}
