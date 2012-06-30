package org.androidtransfuse.integrationTest.activity;

import android.os.Bundle;
import android.widget.TextView;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.integrationTest.R;

/**
 * Tests registration of a regular Android Activty to the Manifest without code generation.
 *
 * @author John Ericksen
 */
@Activity(label = "Manifest Managed")
public class ManifestManagedActivity extends android.app.Activity {

    private static final String MANAGED_TEXT = "This Activity's Manifest entry is added automatically";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        TextView textView = (TextView) findViewById(R.id.displayText);

        textView.setText(MANAGED_TEXT);
    }
}
