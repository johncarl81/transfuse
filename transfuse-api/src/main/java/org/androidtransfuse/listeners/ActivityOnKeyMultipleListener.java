package org.androidtransfuse.listeners;

/**
 * @author John Ericksen
 */
public interface ActivityOnKeyMultipleListener {

    @Listener
    boolean onKeyMultiple(int keyCode, int repeatCount, android.view.KeyEvent event);
}
