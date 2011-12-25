package org.androidrobotics.example.simple;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class NotifyOnClickListener implements View.OnClickListener {

    private int id;

    @Inject
    private NotificationManager notificationManager;

    @Inject
    private Context context;

    @Override
    public void onClick(View view) {

        Notification notification = new Notification(R.drawable.icon, "Hello", System.currentTimeMillis());

        //setup return from notification
        Intent notificationIntent = new Intent(context, SimpleActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        notification.setLatestEventInfo(context, "Simple Service", "Hello", contentIntent);


        notificationManager.notify("test notification", id++, notification);
    }
}
