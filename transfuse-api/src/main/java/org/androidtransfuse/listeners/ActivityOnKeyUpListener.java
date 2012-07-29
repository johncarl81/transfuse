package org.androidtransfuse.listeners;

/**
 * @author John Ericksen
 */
public interface ActivityOnKeyUpListener {

    @Listener
    boolean onKeyUp(int keyCode, android.view.KeyEvent event);
}