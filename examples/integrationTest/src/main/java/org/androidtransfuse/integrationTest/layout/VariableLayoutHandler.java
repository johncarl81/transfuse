package org.androidtransfuse.integrationTest.layout;

import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.layout.LayoutHandlerDelegate;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableLayoutHandler implements LayoutHandlerDelegate {

    @Inject
    private VariableLayoutDependency dependency;
    private boolean getLayoutCalled = false;

    @Override
    public int getLayout() {
        getLayoutCalled = true;
        return R.layout.main;
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
}
