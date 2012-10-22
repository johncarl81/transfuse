package org.androidtransfuse.event;

import org.androidtransfuse.util.TransfuseRuntimeException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author John Ericksen
 */
//todo: fix @Singleton with provider
public class EventManager {

    private final ReadWriteLock observersLock = new ReentrantReadWriteLock();
    private final ConcurrentMap<Class, Set<EventObserver>> observers = new ConcurrentHashMap<Class, java.util.Set<EventObserver>>();
    private final ThreadLocal<ConcurrentLinkedQueue<EventExecution>> executionQueue = new ExecutionQueueThreadLocal();
    private final ThreadLocal<Boolean> executing = new BooleanThreadLocal();

    private static final class EventExecution{
        private final Object event;
        private final EventObserver observer;

        private EventExecution(Object event, EventObserver observer) {
            this.event = event;
            this.observer = observer;
        }

        public void trigger() {
            try{
                observer.trigger(event);
            }
            catch (Exception e){
                throw new TransfuseRuntimeException("Exception caught during event trigger", e);
            }
        }
    }

    public <T> void register(Class<T> event, EventObserver<T> observer){
        if(event == null){
            throw new IllegalArgumentException("Null Event type passed to register");
        }
        if(observer == null){
            throw new IllegalArgumentException("Null observer passed to register");
        }
        observersLock.writeLock().lock();
        try{
            nullSafeGet(event).add(observer);
        }
        finally {
            observersLock.writeLock().unlock();
        }
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

        observersLock.readLock().lock();
        try{
            for (Class eventType : eventTypes) {
                if(observers.containsKey(eventType)){
                    for (EventObserver eventObserver : observers.get(eventType)) {
                        executionQueue.get().add(new EventExecution(event, eventObserver));
                    }
                }
            }

            triggerQueue();
        }
        finally{
            observersLock.readLock().unlock();
        }
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
        observersLock.writeLock().lock();
        try{
            Iterator<Map.Entry<Class,Set<EventObserver>>> entryIterator = observers.entrySet().iterator();
            while(entryIterator.hasNext()){
                Map.Entry<Class, Set<EventObserver>> entry = entryIterator.next();
                entry.getValue().remove(observer);
            }
        }
        finally{
            observersLock.writeLock().unlock();
        }

    }

    private static class BooleanThreadLocal extends ThreadLocal<Boolean>{
        @Override
        protected Boolean initialValue() {
            return false;
        }
    }

    private static class ExecutionQueueThreadLocal extends ThreadLocal<ConcurrentLinkedQueue<EventExecution>> {
        @Override
        protected ConcurrentLinkedQueue<EventExecution> initialValue() {
            return new ConcurrentLinkedQueue<EventExecution>();
        }
    }
}
