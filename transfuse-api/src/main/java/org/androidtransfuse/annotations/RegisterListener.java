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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Specifies the given type, field or method return type as a listener to the identified component.  This annotation
 * triggers Transfuse to investigate the interfaces implemented by the given type.  If the interfaces match any of the
 * available listeners or Call-Through methods, the appropriate registration method will be used to make the annotated
 * instance a listener of the component
 *
 * As an example, one may register an anonymous inner class OnClick Listener as follows:
 * [source,java]
 * ----
 *     {@literal @}RegisterListener(R.id.button)
 *     View.OnClickListener listener = new View.OnClickListener() {
 *         public void onClick(View v) {...}
 *     };
 * ----
 * Likewise, one may register an injected listener the same way:
 * [source,java]
 * ----
 *     {@literal @}RegisterListener(R.id.button)
 *     {@literal @}Inject
 *     ButtonOnClickListener listener;
 * ----
 *
 *
 * @author John Ericksen
 */
@Target({FIELD, TYPE, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterListener {

    /**
     * Resource id used to lookup the View via the `Activity.getViewById()` method.
     */
    int value() default -1;

    /**
     * Resource tag used to lookup the View via the `Activity.getViewByTag()` method.
     */
    String tag() default "";

    /**
     * Listener interfaces to use.  If none are specified, all Listener interfaces will be registered.
     */
    Class[] interfaces() default {};
}
