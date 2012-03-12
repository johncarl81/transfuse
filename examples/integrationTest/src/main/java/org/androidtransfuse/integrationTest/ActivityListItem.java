package org.androidtransfuse.integrationTest;

import android.content.Intent;

public class ActivityListItem {

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
}