package org.androidtransfuse.listeners;

/**
 * @author John Ericksen
 */
public interface ActivityOnTouchEventListener {

    @Listener
    boolean onTouchEvent(android.view.MotionEvent event);
}
