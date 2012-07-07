package org.androidtransfuse.integrationTest.saveState;

/**
 * @author John Ericksen
 */
public class ProgressEvent {

    private int progress;

    public ProgressEvent(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }
}
