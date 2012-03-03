package org.androidtransfuse.integrationTest.inject;

import android.content.Context;
import android.location.LocationManager;
import android.os.Vibrator;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.SystemService;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(name = "SystemInjectionActivity")
@Layout(R.layout.main)
public class SystemInjection {

    @Inject
    private Vibrator vibrator;

    @Inject
    private LocationManager locationManager;

    @Inject
    @SystemService(Context.NOTIFICATION_SERVICE)
    private Object notificationService;

    public Vibrator getVibrator() {
        return vibrator;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public Object getNotificationService() {
        return notificationService;
    }
}
