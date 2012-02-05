package org.androidtransfuse.analysis.targets;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class MockAnalysisSuperClass {

    @Inject
    private int superVariable;

    @Inject
    private void setValue(int value) {
    }
}
