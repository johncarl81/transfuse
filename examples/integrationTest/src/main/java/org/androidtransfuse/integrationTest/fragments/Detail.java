package org.androidtransfuse.integrationTest.fragments;

import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import org.androidtransfuse.annotations.Extra;
import org.androidtransfuse.annotations.LayoutHandler;
import org.androidtransfuse.annotations.OnCreate;
import org.androidtransfuse.annotations.View;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

@org.androidtransfuse.annotations.Activity(type = FragmentActivity.class)
@LayoutHandler(FragmentActivityLayoutHandler.class)
public class Detail {

    @Inject
    @Extra(value = "value", optional = true)
    private String value;

    @Inject
    @View(R.id.detailsText)
    private TextView view;

    @OnCreate
	protected void onCreate() {
		if (value != null) {
			view.setText(value);
		}
	}
}