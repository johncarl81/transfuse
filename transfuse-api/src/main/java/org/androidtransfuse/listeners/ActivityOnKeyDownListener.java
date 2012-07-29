package org.androidtransfuse.listeners;

/**
 * @author John Ericksen
 */
public interface ActivityOnKeyDownListener {

    @Listener
    boolean onKeyDown(int keyCode, android.view.KeyEvent event);
}
