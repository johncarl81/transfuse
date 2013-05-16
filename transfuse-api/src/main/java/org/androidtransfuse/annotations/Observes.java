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
package org.androidtransfuse.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a method to observe an event triggered via the `EventManager` class when registered.  The annotated
 * method must have one parameter which represents the type of event to be observed.
 *
 * For Instance, The following class:
 * [source,java]
 * ----
 *     public class Listener{
 *         {@literal @}Observes
 *         public void listen(Event event){...}
 *     }
 * ----
 * When registered:
 * [source,java]
 * ----
 *     eventManager.register(Event.class, listener);
 * ----
 * The `listen()` method is called when an event is triggered on the related EventManager class:
 * [source,java]
 * ----
 *     Event event = new Event();
 *     eventManager.trigger(event);
 * ----
 *
 * Optionally, the parameter of the method may be annotated to achieve the same effect:
 * [source,java]
 * ----
 *     public void listen({@literal @}Observes} Event event){...}
 * ----
 *
 * Transfuse automatically performs this registration housekeeping with a default, global singleton version of the
 * `EventManager` if the annotated method appears on an instance  injected by Transfuse.
 *
 * @see org.androidtransfuse.event.EventManager
 *
 * @author John Ericksen
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Observes {}
