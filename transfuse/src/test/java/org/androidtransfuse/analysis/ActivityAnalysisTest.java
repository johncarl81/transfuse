package org.androidtransfuse.analysis;

import android.app.Activity;
import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.targets.MockActivityDelegate;
import org.androidtransfuse.model.ComponentDescriptor;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class ActivityAnalysisTest {

    public static final String TEST_NAME = "ActivityTestTarget";
    public static final int TEST_LAYOUT_ID = 123456;

    @Inject
    private ActivityAnalysis activityAnalysis;
    @Inject
    private ASTClassFactory astClassFactory;

    @Before
    public void setup() {
        TransfuseTestInjector.inject(this);
    }

    @Test
    public void testActivityAnnotation() {
        ComponentDescriptor activityDescriptor = activityAnalysis.analyze(astClassFactory.buildASTClassType(MockActivityDelegate.class));
        assertEquals(TEST_NAME, activityDescriptor.getPackageClass().getClassName());

        assertEquals(Activity.class.getName(), activityDescriptor.getType());

        //todo:fill in
    }
}
