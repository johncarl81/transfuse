package org.androidtransfuse.integrationTest.fragments;

import android.util.Log;
import android.widget.TextView;
import org.androidtransfuse.annotations.Fragment;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnActivityCreated;
import org.androidtransfuse.annotations.OnCreate;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

@Fragment
@Layout(R.layout.details)
public class DetailFragment {

    @Inject
    private android.support.v4.app.Fragment fragment;

	@OnCreate
	public void onCreate() {
		Log.i("fragments", "onCreate");
	}

	@OnActivityCreated
	public void onActivityCreated() {
        Log.i("fragments", "onActivityCreated");
	}

	public void setText(String item) {
		TextView view = (TextView) fragment.getView().findViewById(R.id.detailsText);
		view.setText(item);
	}
}