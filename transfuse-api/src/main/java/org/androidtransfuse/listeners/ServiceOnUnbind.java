package org.androidtransfuse.listeners;

import android.content.Intent;

/**
 * @author John Ericksen
 */
public interface ServiceOnUnbind {

    @Listener
    boolean onUnbind(Intent intent);
}
