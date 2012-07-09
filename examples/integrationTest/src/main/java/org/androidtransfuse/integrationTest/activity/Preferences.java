package org.androidtransfuse.integrationTest.activity;

import android.preference.PreferenceActivity;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.OnCreate;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.util.DeclareField;

import javax.inject.Inject;

/**
 * Tests registration of a Special Activity type, PreferenceActivity.
 *
 * @author John Ericksen
 */
@Activity(type = PreferenceActivity.class, label = "Preferences")
@DeclareField
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
