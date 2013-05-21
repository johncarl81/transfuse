/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.event;

import org.androidtransfuse.util.TransfuseRuntimeException;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Centralized Bus for registering, unregistering and triggering events.  An event may be any object and the EventManager
 * triggers observers based on the non-generic type of the event object.  Observers may be registered by calling
 * the `register()` method, which relates an observer to an event type.
 *
 * Transfuse will generate the calls to `register()` and `unregister()` based on the `@Observes`
 * annotation.  Any method in an object managed by Transfuse annotated with `@Observes` will register a call
 * to that method with the first parameter in the method as the Event type.
 *
 * For instance:
 * [source,java]
 * --
 * @Observes
 * public void drink(Coffee coffee){...}
 * --
 *
 * will result in Transfuse generating a the following `EventObserver`:
 *
 * [source,java]
 * --
 * public class DrinkCoffeeEventObserver<Coffee>{
 *     //...
 *     void trigger(Coffee coffee){
 *         managedInstance.drink(coffee);
 *     }
 * }
 * --
 *
 * and the following registration:
 *
 *
 * [source,java]
 * --
 * eventManager.register(Coffee.class, drinkCoffeeEventObserver);
 * --
 *
 *
 * @author John Ericksen
 */
@Singleton
public class EventManager {

    private final ReadWriteLock observersLock = new ReentrantReadWriteLock();
    private final ConcurrentMap<Class, Set<EventObserver>> observers = new ConcurrentHashMap<Class, java.util.Set<EventObserver>>();
    private final ThreadLocal<ConcurrentLinkedQueue<EventExecution>> executionQueue = new ExecutionQueueThreadLocal();
    private final ThreadLocal<Boolean> executing = new BooleanThreadLocal();

    private static final class EventExecution<T>{
        private final T event;
        private final EventObserver<T> observer;

        private EventExecution(T event, EventObserver<T> observer) {
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

    /**
     * Register the given observer to be triggered if the given event type is triggered.
     *
     * @param event type
     * @param observer event observer
     * @param <T> relating type
     */
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

    /**
     * Triggers an event through the EventManager.  This will call the registered EventObservers with the provided
     * event.
     *
     * @param event object
     */
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

    /**
     * Unregisters an EventObserver by equality.
     *
     * @param observer Event Observer
     */
    public void unregister(EventObserver<?> observer){
        observersLock.writeLock().lock();
        try{
            for (Map.Entry<Class, Set<EventObserver>> entry : observers.entrySet()) {
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
