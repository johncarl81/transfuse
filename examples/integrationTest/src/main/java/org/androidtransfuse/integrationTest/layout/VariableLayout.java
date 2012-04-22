package org.androidtransfuse.integrationTest.layout;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.LayoutHandler;
import org.androidtransfuse.annotations.OnPause;

/**
 * @author John Ericksen
 */
@Activity
@LayoutHandler(VariableLayoutHandler.class)
public class VariableLayout {

    @OnPause
    public void keepInActivity() {
    }
}
