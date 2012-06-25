package org.androidtransfuse.integrationTest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import org.androidtransfuse.integrationTest.R;

/**
 * Tests and demonstrates that you may still use and mix the regular Android Activities with Transfuse.
 *
 * @author John Ericksen
 */
public class NotManagedActivity extends Activity {

    private static final String MANAGED_TEXT = "This Activity's Manifest entry is not added, but it is possible to add it yourself";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        TextView textView = (TextView) findViewById(R.id.displayText);

        textView.setText(MANAGED_TEXT);
    }
}
