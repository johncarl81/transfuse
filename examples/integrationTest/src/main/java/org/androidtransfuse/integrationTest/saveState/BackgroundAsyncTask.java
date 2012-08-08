package org.androidtransfuse.integrationTest.saveState;

import android.os.AsyncTask;
import android.os.SystemClock;
import org.androidtransfuse.event.EventManager;

import javax.inject.Inject;

public class BackgroundAsyncTask extends AsyncTask<Object, Integer, ResetEvent> {

    private static final int SLEEP_STEPS = 100;
    private static final int SLEEP_TIME = 200;

    @Inject
    private EventManager eventManager;
    private boolean running = false;
    private boolean paused = false;

    @Override
    protected ResetEvent doInBackground(Object... params) {
        int progress = 0;
        running = true;
        broadcastUpdate();
        while (progress < SLEEP_STEPS) {
            if(!paused){
                progress++;
                eventManager.trigger(new ProgressEvent(progress));
            }
            SystemClock.sleep(SLEEP_TIME);
            if(isCancelled()){
                return null;
            }
        }
        running = false;
        broadcastUpdate();
        return new ResetEvent();
    }

    @Override
    protected void onPostExecute(ResetEvent resetEvent) {
        eventManager.trigger(resetEvent);
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized void togglePause() {
        paused = !paused;
        broadcastUpdate();
    }

    private void broadcastUpdate(){
        eventManager.trigger(new UpdateEvent(paused));
    }
}