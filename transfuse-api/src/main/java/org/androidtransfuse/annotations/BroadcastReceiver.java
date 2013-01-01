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
 * Defines a class to be a Transfuse Broadcast Receiver component.  For classes that do not extent the
 * android.app.BroadcastReceiver class this annotation activates the event systems, dependency injection features and
 * manifest management.
 *
 * For classes that do extend the android.app.BroadcastReceiver class, defining a class as a Transfuse Service will simply
 * activate manifest management of the Broadcast Receiver.
 *
 * Under both cases you may define additional manifest metadata which will be associated with the Broadcast Receiver manifest
 * entry.
 *
 * @author John Ericksen
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BroadcastReceiver {
    String name() default "";

    String label() default "";

    String permission() default "";

    String process() default "";

    String icon() default "";

    boolean enabled() default true;

    boolean exported() default true;

    Class<? extends android.content.BroadcastReceiver>  type() default android.content.BroadcastReceiver.class;
}
