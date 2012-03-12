package org.androidtransfuse.integrationTest;

import android.content.Intent;

public class ActivityListItem implements Comparable<ActivityListItem> {

    private Intent intent;
    private String name;

    public ActivityListItem(Intent intent, String name) {
        this.intent = intent;
        this.name = name;
    }

    public Intent getIntent() {
        return intent;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(ActivityListItem activityListItem) {
        return getName().compareTo(activityListItem.getName());
    }
}