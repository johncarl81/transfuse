package org.androidtransfuse.integrationTest.activity;

import android.preference.PreferenceActivity;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.integrationTest.R;

/**
 * @author John Ericksen
 */
@Activity(type = PreferenceActivity.class)
@Layout(R.layout.main)
public class Preferences {
}
