package org.androidtransfuse.integrationTest;

import android.content.Intent;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ActivityListItem)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        ActivityListItem rhs = (ActivityListItem) obj;
        return new EqualsBuilder()
                .append(name, rhs.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).hashCode();
    }
}