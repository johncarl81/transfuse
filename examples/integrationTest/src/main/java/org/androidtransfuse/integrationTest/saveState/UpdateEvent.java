package org.androidtransfuse.integrationTest.saveState;

/**
 * @author John Ericksen
 */
public class UpdateEvent {
    private boolean paused;

    public UpdateEvent(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }
}
