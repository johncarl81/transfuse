package org.androidtransfuse.integrationTest.inject;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.View;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.util.DeclareField;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(label = "CustomView")
@Layout(R.layout.customview)
@DeclareField
public class CustomView {

    @Inject
    @View(R.id.labelview)
    private LabelView labelView;

    public LabelView getLabelView() {
        return labelView;
    }
}
