package org.androidtransfuse.event;

import android.util.Log;

import javax.inject.Singleton;
import java.util.*;

/**
 * @author John Ericksen
 */
@Singleton
public class EventManager {

    public static final String REGISTER_METHOD = "register";
    public static final String TRIGGER_METHOD = "trigger";

    private Map<Class, Set<EventObserver>> observers = new HashMap<Class, Set<EventObserver>>();

    public <T> void register(Class<T> event, EventObserver<T> observer){
        if(!observers.containsKey(event)){
            observers.put(event, new HashSet<EventObserver>());
        }
        observers.get(event).add(observer);
    }

    public void trigger(Object event){
        if(observers.containsKey(event.getClass())){
            Set<EventObserver> eventObservers = observers.get(event.getClass());

            for (EventObserver eventObserver : eventObservers) {
                try{
                    eventObserver.trigger(event);
                }
                catch (RuntimeException e){
                    Log.e("Observes", "Uncaught RuntimeException in event observer", e);
                }
            }
        }
    }

    public void unregister(EventObserver<?> observer){
        Iterator<Map.Entry<Class,Set<EventObserver>>> entryIterator = observers.entrySet().iterator();
        while(entryIterator.hasNext()){
            Map.Entry<Class, Set<EventObserver>> entry = entryIterator.next();
            entry.getValue().remove(observer);
            if(entry.getValue().isEmpty()){
                entryIterator.remove();
            }
        }

    }
}
