package org.androidtransfuse.analysis;

import com.google.inject.Injector;
import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.targets.MockActivityDelegate;
import org.androidtransfuse.model.ComponentDescriptor;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class ActivityAnalysisTest {

    public static final String TEST_NAME = "ActivityTestTarget";
    public static final int TEST_LAYOUT_ID = 123456;

    private ComponentDescriptor activityDescriptor;

    @Before
    public void setup() {
        Injector injector = TransfuseTestInjector.getInjector(this);

        ActivityAnalysis activityAnalysis = injector.getInstance(ActivityAnalysis.class);

        ASTClassFactory astClassFactory = injector.getInstance(ASTClassFactory.class);

        activityDescriptor = activityAnalysis.analyze(astClassFactory.buildASTClassType(MockActivityDelegate.class));
    }

    @Test
    public void testActivityAnnotation() {
        assertEquals(TEST_NAME, activityDescriptor.getPackageClass().getClassName());
    }
}
