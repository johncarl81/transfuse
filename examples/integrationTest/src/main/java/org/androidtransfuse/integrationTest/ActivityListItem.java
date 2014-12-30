/**
 * Copyright 2011-2015 John Ericksen
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