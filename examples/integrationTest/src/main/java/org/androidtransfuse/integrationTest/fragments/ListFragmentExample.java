package org.androidtransfuse.integrationTest.fragments;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.ArrayAdapter;
import org.androidtransfuse.annotations.Fragment;
import org.androidtransfuse.annotations.OnActivityCreated;
import org.androidtransfuse.annotations.OnListItemClick;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.intentFactory.IntentFactory;

import javax.inject.Inject;
import java.lang.reflect.Field;

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

    @OnActivityCreated
    public void onActivityCreated() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, VALUES);
        listFragment.setListAdapter(adapter);
    }

    @OnListItemClick
    public void onListItemClick(int position) {
        String item = (String) listFragment.getListAdapter().getItem(position);
        DetailFragmentFragment fragment = (DetailFragmentFragment) fragmentManager.findFragmentById(R.id.detailFragment);
        if (fragment != null && fragment.isInLayout()) {
            try {
                Field detailFragment_0 = DetailFragmentFragment.class.getDeclaredField("detailFragment_0");
                detailFragment_0.setAccessible(true);
                ((DetailFragment) detailFragment_0.get(fragment)).setText(item);
            } catch (NoSuchFieldException e) {
                Log.e("error", "NoSuchFieldException while accessing detailFragment", e);
            } catch (IllegalAccessException e) {
                Log.e("error", "IllegalAccessException while accessing detailFragment", e);
            }
        } else {
            intentFactory.start(new DetailActivityStrategy().setValue(item));
        }
    }
}