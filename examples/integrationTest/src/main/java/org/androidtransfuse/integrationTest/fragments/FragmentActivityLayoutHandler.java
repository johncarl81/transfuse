package org.androidtransfuse.integrationTest.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.layout.LayoutHandlerDelegate;

import javax.inject.Inject;

public class FragmentActivityLayoutHandler implements LayoutHandlerDelegate {

    @Inject
    private Activity activity;

    @Override
    public void invokeLayout() {
        // Need to check if Activity has been switched to landscape mode
        // If yes, finished and go back to the start Activity

        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.finish();
        }
        else{
            activity.setContentView(R.layout.details_activity_layout);
        }
    }
}