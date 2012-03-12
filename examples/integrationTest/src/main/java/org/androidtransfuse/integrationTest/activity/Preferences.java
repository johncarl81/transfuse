package org.androidtransfuse.integrationTest.activity;

import android.preference.PreferenceActivity;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.OnCreate;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(type = PreferenceActivity.class)
public class Preferences {

    @Inject
    private PreferenceActivity activity;

    @OnCreate
    public void setupPreferences() {
        activity.addPreferencesFromResource(R.xml.preferences);
    }

    public PreferenceActivity getActivity() {
        return activity;
    }
}
