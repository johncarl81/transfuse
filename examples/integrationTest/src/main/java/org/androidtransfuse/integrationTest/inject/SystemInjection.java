package org.androidtransfuse.integrationTest.inject;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.intentFactory.IntentFactory;

import javax.inject.Inject;

import static org.androidtransfuse.integrationTest.SharedVariables.ONE_SECOND;

/**
 * @author John Ericksen
 */
@Activity(name = "SystemInjectionActivity", label = "System Services")
@Layout(R.layout.system)
public class SystemInjection {

    private static final int NOTIFICATION_ID = 42;

    private Vibrator vibrator;

    @Inject
    private LocationManager locationManager;

    @Inject
    @SystemService(Context.NOTIFICATION_SERVICE)
    private Object notificationService;

    @Inject
    private Context context;

    @Inject
    private Application application;

    @Inject
    private IntentFactory intentFactory;

    @Inject
    private LocationToaster locationToaster;

    public Vibrator getVibrator() {
        return vibrator;
    }

    @RegisterListener(R.id.vibratebutton)
    private View.OnClickListener vibrateOnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            vibrator.vibrate(ONE_SECOND);
        }
    };

    @RegisterListener(R.id.notificationbutton)
    private View.OnClickListener notificationOnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Notification notification = new Notification(R.drawable.icon, "Notification", System.currentTimeMillis());

            //setup return from notification
            PendingIntent contentIntent = intentFactory.buildPendingIntent(new SystemInjectionActivityStrategy());

            notification.setLatestEventInfo(context, "Transfuse Integration Application", "Notification", contentIntent);

            ((NotificationManager)notificationService).notify("Transfuse Notification", NOTIFICATION_ID, notification);
        }
    };

    @RegisterListener(R.id.locationbutton)
    private View.OnClickListener locationOnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setCostAllowed(false);

            String providerName = locationManager.getBestProvider(criteria, true);

            String results;
            if (providerName != null) {
                locationManager.requestLocationUpdates(providerName,
                        10000,          // 10-second interval.
                        10,             // 10 meters.
                        locationToaster);
            }
            else{
                Toast.makeText(context, "Could not find location provider", ONE_SECOND).show();
            }
        }
    };

    @OnPause
    public void keepInActivity() {
    }

    @Inject
    public void setVibrator(Vibrator vibrator) {
        this.vibrator = vibrator;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public Object getNotificationService() {
        return notificationService;
    }

    public Context getContext() {
        return context;
    }

    public Application getApplication() {
        return application;
    }
}
