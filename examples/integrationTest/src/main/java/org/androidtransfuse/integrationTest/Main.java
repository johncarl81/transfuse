package org.androidtransfuse.integrationTest;

import android.app.ListActivity;
import android.widget.ArrayAdapter;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.activity.ManifestManagedActivity;
import org.androidtransfuse.integrationTest.activity.NotManagedActivity;
import org.androidtransfuse.integrationTest.activity.PreferencesActivity;
import org.androidtransfuse.integrationTest.aop.AOPActivity;
import org.androidtransfuse.integrationTest.broadcastReceiver.ToastTriggerActivity;
import org.androidtransfuse.integrationTest.fragments.FragmentMainActivity;
import org.androidtransfuse.integrationTest.inject.*;
import org.androidtransfuse.integrationTest.layout.VariableLayoutActivity;
import org.androidtransfuse.integrationTest.lifecycle.ActivityLifecycleActivity;
import org.androidtransfuse.integrationTest.observes.EventObserverActivity;
import org.androidtransfuse.integrationTest.register.RegisterActivity;
import org.androidtransfuse.integrationTest.saveState.NonConfigInstanceActivity;
import org.androidtransfuse.integrationTest.saveState.SaveInstanceStateActivity;
import org.androidtransfuse.integrationTest.scope.ScopeOneActivity;
import org.androidtransfuse.integrationTest.scope.ScopeTwoActivity;
import org.androidtransfuse.intentFactory.IntentFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author John Ericksen
 */
@Activity(label = "@string/app_name", type = ListActivity.class)
@IntentFilter({
        @Intent(type = IntentType.ACTION, name = android.content.Intent.ACTION_MAIN),
        @Intent(type = IntentType.CATEGORY, name = android.content.Intent.CATEGORY_LAUNCHER)
})
public class Main {

    private static final long THE_ANSWER = 42;
    private static final String TEST_VALUE = "test value";

    private List<ActivityListItem> values;
    private ListActivity listActivity;
    private IntentFactory intentFactory;

    @Inject
    public Main(ListActivity listActivity, IntentFactory intentFactory) {

        this.intentFactory = intentFactory;
        this.listActivity = listActivity;

        values = new ArrayList<ActivityListItem>(Arrays.asList(new ActivityListItem[]{
                createLI(PreferencesActivity.class, "Preferences"),
                createLI(AOPActivity.class, "AOP"),
                createExtraLI(),
                createLI(InjectionActivity.class, "Injection"),
                createLI(ResourceInjectionActivity.class, "Resources"),
                createLI(SystemInjectionActivity.class, "System Services"),
                createLI(ActivityLifecycleActivity.class, "Activity Lifecycle"),
                createLI(ScopeOneActivity.class, "Scope One"),
                createLI(ScopeTwoActivity.class, "Scope Two"),
                createLI(NotManagedActivity.class, "Not Managed"),
                createLI(CustomViewActivity.class, "Custom View"),
                createLI(VariableLayoutActivity.class, "Variable Layout"),
                createLI(RegisterActivity.class, "Listener Registration"),
                createLI(ManifestManagedActivity.class, "Manifest Managed Activity"),
                createLI(ToastTriggerActivity.class, "Broadcast Receiver"),
                createLI(PreferenceInjectionActivity.class, "Preference Injection"),
                createLI(ViewInjectionActivity.class, "View Injection"),
                createLI(FragmentMainActivity.class, "Fragments"),
                createLI(EventObserverActivity.class, "Event Observer"),
                createLI(SaveInstanceStateActivity.class, "Save Instance State"),
                createLI(NonConfigInstanceActivity.class, "NonConfigurationInstance task"),
                createLI(AboutActivity.class, "About")
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

    private ActivityListItem createExtraLI() {
        ParcelExample parcelExample = new ParcelExample();

        parcelExample.setInnerParcel(new ParcelTwo(TEST_VALUE));
        parcelExample.setName(TEST_VALUE);
        parcelExample.setValue(THE_ANSWER);
        parcelExample.setSerializableValue(new SerializableValue(TEST_VALUE));
        parcelExample.setRealParcelable(new RealParcelable(TEST_VALUE));

        android.content.Intent intent = intentFactory.buildIntent(new ExtraInjectionActivityStrategy(new SerializableValue(TEST_VALUE), TEST_VALUE, parcelExample, THE_ANSWER));
        return new ActivityListItem(intent, "Extras");
    }
}
