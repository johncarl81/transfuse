package org.androidtransfuse.integrationTest.inject;

import android.widget.TextView;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity
@Layout(R.layout.preferenceinjection)
public class PreferenceInjection {

    @Inject
    @View(R.id.preferenceText)
    private TextView preferenceText;

    @Inject
    @Preference(value = "pref_dialog", defaultValue = "test")
    private String preference;

    @OnCreate
    public void updatePreferenceText(){
        preferenceText.setText("Preference Value: " + preference);
    }
}
