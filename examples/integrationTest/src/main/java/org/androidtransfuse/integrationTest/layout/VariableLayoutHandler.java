package org.androidtransfuse.integrationTest.layout;

import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.layout.LayoutHandlerDelegate;

/**
 * @author John Ericksen
 */
public class VariableLayoutHandler implements LayoutHandlerDelegate {
    @Override
    public int getLayout() {
        return R.layout.main;
    }
}
