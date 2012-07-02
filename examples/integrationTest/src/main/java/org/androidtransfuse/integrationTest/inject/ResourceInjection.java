package org.androidtransfuse.integrationTest.inject;

import android.widget.TextView;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(name = "ResourceInjectionActivity", label = "Resources")
@Layout(R.layout.display)
public class ResourceInjection {

    @Inject
    @Resource(R.string.app_name)
    private String appName;

    @Inject
    @View(R.id.displayText)
    private TextView textView;

    public String getAppName() {
        return appName;
    }

    @OnCreate
    public void updateDisplayText(){
        textView.setText("Text Resource: " + appName);
    }

    @OnPause
    public void keepInActivity() {
    }
}
