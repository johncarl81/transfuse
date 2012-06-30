package org.androidtransfuse.integrationTest.layout;

import android.app.Activity;
import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.layout.LayoutHandlerDelegate;

import javax.inject.Inject;
import java.util.Random;

/**
 * @author John Ericksen
 */
public class VariableLayoutHandler implements LayoutHandlerDelegate {

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

    @OnPause
    public void keepInActivity() {
    }

    public boolean isGetLayoutCalled() {
        return getLayoutCalled;
    }

    public Activity getActivity() {
        return activity;
    }

    public int getRandomLayout() {
        switch (rand.nextInt(3)){
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
