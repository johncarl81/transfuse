package org.androidtransfuse.integrationTest.activity;

import android.app.Activity;
import android.os.Bundle;
import org.androidtransfuse.integrationTest.R;

/**
 * @author John Ericksen
 */
public class NotManagedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
