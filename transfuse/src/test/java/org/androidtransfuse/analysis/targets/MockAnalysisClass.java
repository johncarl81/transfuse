package org.androidtransfuse.analysis.targets;

import org.androidtransfuse.util.TransfuseInjectionException;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class MockAnalysisClass extends MockAnalysisSuperClass {

    private MockParameter testParameter;

    @Inject
    public MockAnalysisClass(MockParameter testParameter) throws TransfuseInjectionException {
        this.testParameter = testParameter;
    }

    @Inject
    public void injectMethod(MockParameter testParameter) throws TransfuseInjectionException {
        this.testParameter = testParameter;
    }
}
