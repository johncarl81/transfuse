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
 * The annotation representation of the Android Manifest IntentFilter tag.  This annotation may be used along with the
 * `@Activity`, `@Service`, or `@BroadcastReceiver` to define the Intents that may activate this
 * Component.
 *
 * The following example demonstrates an Activity as the default home screen of an application available on the Launcher:
 *
 * [source,java]
 * .*Example:*
 * --
 * @Activity
 * @IntentFilter({
 *     @Intent(type = IntentType.ACTION, name = android.content.Intent.ACTION_MAIN),
 *     @Intent(type = IntentType.CATEGORY, name = android.content.Intent.CATEGORY_LAUNCHER)
 * })
 * public class Main {}
 * --
 *
 *
 * @author John Ericksen
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntentFilter {

    String icon() default "";

    String label() default "";

    int priority() default -1;

    Intent[] value();
}
