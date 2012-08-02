package org.androidtransfuse.event;

import android.util.Log;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author John Ericksen
 */
//todo: fix @Singleton with provider
public class EventManager {

    public static final String REGISTER_METHOD = "register";
    public static final String TRIGGER_METHOD = "trigger";

    private final ConcurrentHashMap<Class, Set<EventObserver>> observers = new ConcurrentHashMap<Class, java.util.Set<EventObserver>>();

    public <T> void register(Class<T> event, EventObserver<T> observer){
        nullSafeGet(event).add(observer);
    }

    private Set<EventObserver> nullSafeGet(Class<?> clazz) {
        Set<EventObserver> result = observers.get(clazz);
        if (result == null) {
            Set<EventObserver> value = new CopyOnWriteArraySet<EventObserver>();
            result = observers.putIfAbsent(clazz, value);
            if (result == null) {
                result = value;
            }
        }
        return result;
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
