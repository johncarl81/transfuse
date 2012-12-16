/**
 * Copyright 2012 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.RegisterListener;
import org.androidtransfuse.annotations.SystemService;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.intentFactory.IntentFactory;
import org.androidtransfuse.util.DeclareField;

import javax.inject.Inject;

import static org.androidtransfuse.integrationTest.SharedVariables.ONE_SECOND;

/**
 * @author John Ericksen
 */
@Activity(name = "SystemInjectionActivity", label = "System Services")
@Layout(R.layout.system)
@DeclareField
public class SystemInjection {

    private static final int NOTIFICATION_ID = 42;
    private static final int MIN_DISTANCE = 10;

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
    private View.OnClickListener vibrateOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            vibrator.vibrate(ONE_SECOND);
        }
    };

    @RegisterListener(R.id.notificationbutton)
    private View.OnClickListener notificationOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Notification notification = new Notification(R.drawable.icon, "Notification", System.currentTimeMillis());

            //setup return from notification
            PendingIntent contentIntent = intentFactory.buildPendingIntent(new SystemInjectionActivityStrategy());

            notification.setLatestEventInfo(context, "Transfuse Integration Application", "Notification", contentIntent);

            ((NotificationManager) notificationService).notify("Transfuse Notification", NOTIFICATION_ID, notification);
        }
    };

    @RegisterListener(R.id.locationbutton)
    private View.OnClickListener locationOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setCostAllowed(false);

            String providerName = locationManager.getBestProvider(criteria, true);

            if (providerName != null) {
                locationManager.requestLocationUpdates(providerName, 0, MIN_DISTANCE, locationToaster);
            } else {
                Toast.makeText(context, "Could not find location provider", ONE_SECOND).show();
            }
        }
    };

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
