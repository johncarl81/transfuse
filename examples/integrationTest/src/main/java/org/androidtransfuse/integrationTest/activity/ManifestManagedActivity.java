package org.androidtransfuse.integrationTest.activity;

import android.os.Bundle;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.integrationTest.R;

/**
 * @author John Ericksen
 */
@Activity
public class ManifestManagedActivity extends android.app.Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
