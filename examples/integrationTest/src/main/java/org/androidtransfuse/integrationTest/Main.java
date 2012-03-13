package org.androidtransfuse.integrationTest;

import android.app.ListActivity;
import android.widget.ArrayAdapter;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.activity.NotManagedActivity;
import org.androidtransfuse.integrationTest.activity.PreferencesActivity;
import org.androidtransfuse.integrationTest.aop.AOPActivity;
import org.androidtransfuse.integrationTest.inject.*;
import org.androidtransfuse.integrationTest.lifecycle.ActivityLifecycleActivity;
import org.androidtransfuse.integrationTest.scope.ScopeOneActivity;
import org.androidtransfuse.integrationTest.scope.ScopeTwoActivity;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author John Ericksen
 */
@Activity(label = "@string/app_name", type = ListActivity.class)
@IntentFilters({
        @Intent(type = IntentType.ACTION, name = "android.intent.action.MAIN"),
        @Intent(type = IntentType.CATEGORY, name = "android.intent.category.LAUNCHER")
})
public class Main {

    private List<ActivityListItem> values;
    private ListActivity listActivity;

    @Inject
    public Main(ListActivity listActivity) {

        this.listActivity = listActivity;

        values = new ArrayList<ActivityListItem>(Arrays.asList(new ActivityListItem[]{
                createLI(PreferencesActivity.class, "Preferences"),
                createLI(AOPActivity.class, "AOP"),
                createExtraLI(ExtraInjectionActivity.class, "Extras"),
                createLI(InjectionActivity.class, "Injection"),
                createLI(ResourceInjectionActivity.class, "Resources"),
                createLI(SystemInjectionActivity.class, "System Services"),
                createLI(ActivityLifecycleActivity.class, "Lifecycle"),
                createLI(ScopeOneActivity.class, "Scope One"),
                createLI(ScopeTwoActivity.class, "Scope Two"),
                createLI(NotManagedActivity.class, "Not Managed")
        }));

        Collections.sort(values);

        ArrayAdapter<ActivityListItem> adapter = new ArrayAdapter<ActivityListItem>(listActivity, android.R.layout.simple_list_item_1, values);
        listActivity.setListAdapter(adapter);
    }

    @OnListItemClick
    public void onListItemClick(int position) {
        listActivity.startActivity(values.get(position).getIntent());
    }

    private ActivityListItem createLI(Class<? extends android.app.Activity> clazz, String name) {
        android.content.Intent intent = new android.content.Intent(listActivity, clazz);
        return new ActivityListItem(intent, name);
    }

    private ActivityListItem createExtraLI(Class<ExtraInjectionActivity> clazz, String name) {
        android.content.Intent intent = new android.content.Intent(listActivity, clazz);

        intent.putExtra(ExtraInjection.EXTRA_ONE, "one");
        intent.putExtra(ExtraInjection.EXTRA_TWO, 42L);
        return new ActivityListItem(intent, name);
    }
}
