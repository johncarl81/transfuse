package org.androidtransfuse.integrationTest.fragments;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;
import org.androidtransfuse.annotations.Fragment;
import org.androidtransfuse.annotations.OnActivityCreated;
import org.androidtransfuse.annotations.OnListItemClick;
import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.intentFactory.IntentFactory;

import javax.inject.Inject;

@Fragment(type = ListFragment.class)
public class ListFragmentExample {

    private static final String[] VALUES = new String[]{"Android", "iPhone", "WindowsMobile",
            "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
            "Linux", "OS/2"};

    @Inject
    private ListFragment listFragment;
    @Inject
    private Activity activity;
    @Inject
    private FragmentManager fragmentManager;
    @Inject
    private IntentFactory intentFactory;
    @Inject
    private EventManager eventManager;

    @OnActivityCreated
    public void onActivityCreated() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, VALUES);
        listFragment.setListAdapter(adapter);
    }

    @OnListItemClick
    public void onListItemClick(int position) {
        android.support.v4.app.Fragment fragment = fragmentManager.findFragmentById(R.id.detailFragment);
        if (fragment != null && fragment.isInLayout()) {
            eventManager.trigger(new TextChange(VALUES[position]));
        } else {
            intentFactory.start(new DetailActivityStrategy().setValue(VALUES[position]));
        }
    }
}