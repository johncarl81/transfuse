package org.androidtransfuse.integrationTest.fragments;

import android.util.Log;
import android.widget.TextView;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

@Fragment
@Layout(R.layout.details)
public class DetailFragment {

    @Inject
    @View(R.id.detailsText)
    private TextView view;

    @OnActivityCreated
    public void onActivityCreated() {
        Log.i("fragments", "onActivityCreated");
    }

    public void setText(@Observes TextChange textChange) {
        view.setText(textChange.getValue());
    }
}