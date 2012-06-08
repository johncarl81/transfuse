package org.androidtransfuse.processor;

import org.androidtransfuse.model.manifest.Activity;
import org.androidtransfuse.model.manifest.Application;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.manifest.Receiver;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
@Singleton
public class ManifestManager {

    private Application application;
    private List<Activity> activities = new ArrayList<Activity>();
    private List<Receiver> broadcastReceivers = new ArrayList<Receiver>();

    public void setApplication(Application application) {
        this.application = application;
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }

    public void addBroadcastReceiver(Receiver broadcastReceiver) {
        this.broadcastReceivers.add(broadcastReceiver);
    }

    public Manifest getManifest() {
        Manifest manifest = new Manifest();

        Application localApplication = application;

        if (application == null) {
            localApplication = new Application();
        }

        localApplication.getActivities().addAll(activities);
        System.out.println("Added: " + activities.size());
        localApplication.getReceivers().addAll(broadcastReceivers);
        System.out.println("Added: " + broadcastReceivers.size());

        manifest.getApplications().add(localApplication);

        return manifest;
    }
}
