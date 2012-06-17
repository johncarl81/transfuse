package org.androidtransfuse.integrationTest.inject;

import android.widget.TextView;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnCreate;
import org.androidtransfuse.annotations.View;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity
@Layout(R.layout.viewinjection)
public class ViewInjection {

    @Inject
    @View(R.id.viewText)
    private TextView textViewById;

    @Inject
    @View(tag = "viewTag")
    private TextView textViewByTag;

    @OnCreate
    public void updateText() {
        textViewById.setText("View by ID");
        textViewByTag.setText("View by Tag");
    }
}
