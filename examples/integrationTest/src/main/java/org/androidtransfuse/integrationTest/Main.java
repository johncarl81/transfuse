/**
 * Copyright 2013 John Ericksen
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

import android.app.ListActivity;
import android.widget.ArrayAdapter;
import org.androidtransfuse.Components;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.activity.ManifestManagedActivity;
import org.androidtransfuse.integrationTest.activity.NotManagedActivity;
import org.androidtransfuse.integrationTest.activity.Preferences;
import org.androidtransfuse.integrationTest.aop.AOP;
import org.androidtransfuse.integrationTest.broadcastReceiver.ToastTrigger;
import org.androidtransfuse.integrationTest.fragments.FragmentMain;
import org.androidtransfuse.integrationTest.inject.*;
import org.androidtransfuse.integrationTest.layout.VariableLayout;
import org.androidtransfuse.integrationTest.lifecycle.ActivityLifecycle;
import org.androidtransfuse.integrationTest.listeners.Listeners;
import org.androidtransfuse.integrationTest.menu.Menu;
import org.androidtransfuse.integrationTest.observes.EventObserver;
import org.androidtransfuse.integrationTest.register.Register;
import org.androidtransfuse.integrationTest.saveState.NonConfigInstance;
import org.androidtransfuse.integrationTest.saveState.SaveInstanceState;
import org.androidtransfuse.integrationTest.scope.ScopeOne;
import org.androidtransfuse.integrationTest.scope.ScopeTwo;
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
                createLI(Components.get(Preferences.class), "Preferences"),
                createLI(Components.get(AOP.class), "AOP"),
                createExtraLI(),
                createLI(Components.get(Injection.class), "Injection"),
                createLI(Components.get(ResourceInjection.class), "Resources"),
                createLI(Components.get(SystemInjection.class), "System Services"),
                createLI(Components.get(ActivityLifecycle.class), "Activity Lifecycle"),
                createLI(Components.get(ScopeOne.class), "Singleton Scope One"),
                createLI(Components.get(ScopeTwo.class), "Singleton Scope Two"),
                createLI(NotManagedActivity.class, "Not Managed"),
                createLI(Components.get(CustomView.class), "Custom View"),
                createLI(Components.get(VariableLayout.class), "Variable Layout"),
                createLI(Components.get(Register.class), "Listener Registration"),
                createLI(ManifestManagedActivity.class, "Manifest Managed Activity"),
                createLI(Components.get(ToastTrigger.class), "Broadcast Receiver"),
                createLI(Components.get(PreferenceInjection.class), "Preference Injection"),
                createLI(Components.get(ViewInjection.class), "View Injection"),
                createLI(Components.get(FragmentMain.class), "Fragments"),
                createLI(Components.get(EventObserver.class), "Event Observer"),
                createLI(Components.get(SaveInstanceState.class), "Save Instance State"),
                createLI(Components.get(NonConfigInstance.class), "NonConfiguration AsyncTask"),
                createLI(Components.get(Listeners.class), "Activity Listeners"),
                createLI(Components.get(Menu.class), "Menu")
        }));

        Collections.sort(values);

        values.add(createLI(Components.get(About.class), "About"));

        ArrayAdapter<ActivityListItem> adapter = new ArrayAdapter<ActivityListItem>(listActivity, android.R.layout.simple_list_item_1, values);
        listActivity.setListAdapter(adapter);
    }

    @OnListItemClick
    public void onListItemClick(int position) {
        listActivity.startActivity(values.get(position).getIntent());
    }

    private ActivityListItem createLI(Class<?> clazz, String name) {
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
