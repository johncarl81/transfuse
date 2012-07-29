package org.androidtransfuse.integrationTest.layout;

import android.app.Activity;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.layout.LayoutHandlerDelegate;
import org.androidtransfuse.util.DeclareField;

import javax.inject.Inject;
import java.util.Random;

/**
 * @author John Ericksen
 */
@DeclareField
public class VariableLayoutHandler implements LayoutHandlerDelegate {

    private static final int TOTAL_LAYOUTS = 3;

    private VariableLayoutDependency dependency;
    private Activity activity;
    private Random rand;
    private boolean getLayoutCalled = false;

    @Inject
    public VariableLayoutHandler(VariableLayoutDependency dependency, Activity activity, Random rand) {
        this.dependency = dependency;
        this.activity = activity;
        this.rand = rand;
    }

    @Override
    public void invokeLayout() {
        getLayoutCalled = true;
        activity.setContentView(getRandomLayout());
    }

    public VariableLayoutDependency getDependency() {
        return dependency;
    }

    public boolean isGetLayoutCalled() {
        return getLayoutCalled;
    }

    public Activity getActivity() {
        return activity;
    }

    public int getRandomLayout() {
        switch (rand.nextInt(TOTAL_LAYOUTS)){
            case 0:
                return R.layout.variablelayoutone;
            case 1:
                return R.layout.variablelayouttwo;
            case 2:
                return R.layout.variablelayoutthree;
            default:
                return R.layout.main;
        }
    }
}
