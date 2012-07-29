package org.androidtransfuse.listeners;

/**
 * @author John Ericksen
 */
public interface ActivityOnKeyLongPressListener {

    @Listener
    boolean onKeyLongPress(int keyCode, android.view.KeyEvent event);
}
