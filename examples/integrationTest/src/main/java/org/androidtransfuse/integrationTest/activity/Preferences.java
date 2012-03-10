package org.androidtransfuse.integrationTest.activity;

import android.preference.PreferenceActivity;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(label = "@string/app_name", type = PreferenceActivity.class)
@IntentFilters({
        @Intent(type = IntentType.ACTION, name = "android.intent.action.MAIN"),
        @Intent(type = IntentType.CATEGORY, name = "android.intent.category.LAUNCHER")
})
public class Preferences {

    @Inject
    private PreferenceActivity activity;

    @OnCreate
    public void setupPreferences() {
        activity.addPreferencesFromResource(R.xml.preferences);
    }
}
