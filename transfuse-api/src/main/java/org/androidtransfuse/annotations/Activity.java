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
 * Defines a class to be a Transfuse Activity component.  For classes that do not extent the android.app.Activity
 * class this annotation activates the event systems, dependency injection features and manifest management.
 *
 * For classes that do extend the android.app.Activity class, defining a class as a Transfuse Activity will simply
 * activate manifest management of the Activity.
 *
 * Under both cases you may define additional manifest metadata which will be associated with the Activity manifest
 * entry.
 *
 * @author John Ericksen
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Activity {
    String name() default "";

    String label() default "";

    boolean allowTaskReparenting() default false;

    boolean alwaysRetainTaskState() default false;
    
    boolean clearTaskOnLaunch() default false;
    
    ConfigChanges[] configChanges() default {};
    
    boolean enabled() default true;
    
    boolean excludeFromRecents() default false;
    
    Exported exported() default Exported.UNSPECIFIED;

    boolean finishOnTaskLaunch() default false;
    
    boolean hardwareAccelerated() default false;
    
    String icon() default "";
    
    LaunchMode launchMode() default LaunchMode.STANDARD;
    
    boolean multiprocess() default false;
    
    boolean noHistory() default false;
    
    String permission() default "";
    
    String process() default "";
    
    ScreenOrientation screenOrientation() default ScreenOrientation.UNSPECIFIED;
    
    boolean stateNotNeeded() default false;
    
    String taskAffinity() default "";
    
    String theme() default "";
    
    UIOptions uiOptions() default UIOptions.NONE;
    
    WindowSoftInputMode windowSoftInputMode() default WindowSoftInputMode.STATE_UNSPECIFIED;

    Class<? extends android.app.Activity> type() default android.app.Activity.class;
}
