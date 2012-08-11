package org.androidtransfuse.event;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author John Ericksen
 */
//todo: fix @Singleton with provider
public class EventManager {

    public static final String REGISTER_METHOD = "register";
    public static final String TRIGGER_METHOD = "trigger";

    private final ConcurrentMap<Class, Set<EventObserver>> observers = new ConcurrentHashMap<Class, java.util.Set<EventObserver>>();

    private final ThreadLocal<ConcurrentLinkedQueue<EventExecution>> executionQueue =
            new ThreadLocal<ConcurrentLinkedQueue<EventExecution>>(){
                @Override
                protected ConcurrentLinkedQueue<EventExecution> initialValue() {
                    return new ConcurrentLinkedQueue<EventExecution>();
                }
            };

    private final ThreadLocal<Boolean> executing = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    private static final class EventExecution{
        private Object event;
        private EventObserver observer;

        private EventExecution(Object event, EventObserver observer) {
            this.event = event;
            this.observer = observer;
        }

        public void trigger() {
            try{
                observer.trigger(event);
            }
            catch (Exception e){
                throw new RuntimeException("Exception caught during event trigger", e);
            }
        }
    }

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

        Set<Class> eventTypes = getAllInheritedClasses(event.getClass());

        for (Class eventType : eventTypes) {
            if(observers.containsKey(eventType)){
                for (EventObserver eventObserver : observers.get(eventType)) {
                    executionQueue.get().add(new EventExecution(event, eventObserver));
                }
            }
        }

        triggerQueue();
    }

    private void triggerQueue(){

        //avoid reentrant events
        if(executing.get()){
            return;
        }

        executing.set(true);

        try{
            EventExecution execution = executionQueue.get().poll();
            while(execution != null){
                execution.trigger();
                execution = executionQueue.get().poll();
            }
        }
        finally{
            executing.set(false);
        }
    }

    private Set<Class> getAllInheritedClasses(Class type){
        Set<Class> inheritedClasses = new HashSet<Class>();

        addAllInheritedClasses(inheritedClasses, type);

        return inheritedClasses;
    }

    private void addAllInheritedClasses(Set<Class> inheritedClasses, Class type){

        if(type != null){
            inheritedClasses.add(type);

            addAllInheritedClasses(inheritedClasses, type.getSuperclass());
            for(Class interf : type.getInterfaces()){
                addAllInheritedClasses(inheritedClasses, interf);
            }
        }
    }

    public void unregister(EventObserver<?> observer){
        Iterator<Map.Entry<Class,Set<EventObserver>>> entryIterator = observers.entrySet().iterator();
        while(entryIterator.hasNext()){
            Map.Entry<Class, Set<EventObserver>> entry = entryIterator.next();
            entry.getValue().remove(observer);
        }

    }
}
