package org.androidtransfuse.integrationTest.fragments;

import android.util.Log;
import android.widget.TextView;
import org.androidtransfuse.annotations.Fragment;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnActivityCreated;
import org.androidtransfuse.annotations.View;
import org.androidtransfuse.integrationTest.R;

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

    public void setText(String item) {
        view.setText(item);
    }
}