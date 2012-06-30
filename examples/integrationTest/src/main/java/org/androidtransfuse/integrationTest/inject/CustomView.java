package org.androidtransfuse.integrationTest.inject;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.annotations.View;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(label = "CustomView")
@Layout(R.layout.customview)
public class CustomView {

    @Inject
    @View(R.id.labelview)
    private LabelView labelView;

    @OnPause
    public void keepInActivity() {
    }

    public LabelView getLabelView() {
        return labelView;
    }
}
