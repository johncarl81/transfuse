package org.androidtransfuse.integrationTest.fragments;

import android.util.Log;
import android.widget.TextView;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.integrationTest.observes.EventOne;

import javax.inject.Inject;

@Fragment
@Layout(R.layout.details)
public class DetailFragment {

    @Inject
    private android.support.v4.app.Fragment fragment;

    @Inject
    @View(R.id.detailsText)
    private TextView view;

    @OnActivityCreated
    public void onActivityCreated() {
        Log.i("fragments", "onActivityCreated");
    }

    public void setText(@Observes EventOne textChange) {
        view.setText(textChange.getValue());
    }
}