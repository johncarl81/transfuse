package org.androidtransfuse.listeners;

/**
 * @author John Ericksen
 */
public interface ServiceOnStartCommand {

    @Listener
    int onStartCommand(android.content.Intent intent, int flags, int startId);
}
