package org.androidtransfuse.integrationTest.inject;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.annotations.Resource;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(name = "ResourceInjectionActivity")
@Layout(R.layout.main)
public class ResourceInjection {

    @Inject
    @Resource(R.string.app_name)
    private String appName;

    public String getAppName() {
        return appName;
    }

    @OnPause
    public void keepInActivity() {
    }
}
