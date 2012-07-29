package org.androidtransfuse.listeners;

import android.view.Menu;
import android.view.MenuItem;

/**
 * @author John Ericksen
 */
public interface ActivityMenuComponent {

    @Listener
    boolean onCreateOptionsMenu(Menu menu);

    @Listener
    boolean onPrepareOptionsMenu(android.view.Menu menu);

    @Listener
    boolean onOptionsItemSelected(MenuItem menuItem);

    @Listener
    boolean onMenuOpened(int featureId, android.view.Menu menu);

    @Listener
    void onOptionsMenuClosed(android.view.Menu menu);


}
