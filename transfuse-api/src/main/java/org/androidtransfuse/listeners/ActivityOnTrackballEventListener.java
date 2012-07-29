package org.androidtransfuse.listeners;

/**
 * @author John Ericksen
 */
public interface ActivityOnTrackballEventListener {

    @Listener
    boolean onTrackballEvent(android.view.MotionEvent event);
}
