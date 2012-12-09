package org.androidtransfuse.integrationTest.externalGenerator;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.util.DeclareField;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity
@Layout(R.layout.main)
@DeclareField
public class Test {

    private Target target;

    @Inject
    public Test(Target target) {
        this.target = target;
    }

    public Target getTarget() {
        return target;
    }
}
