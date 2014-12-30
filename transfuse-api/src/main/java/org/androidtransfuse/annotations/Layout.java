/**
 * Copyright 2011-2015 John Ericksen
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
 * Used in conjunction with the `@Activity` annotation, defines the layout resource to use.  The generated
 * `onCreate()` method will call the `setContentView()` with the provided layout resource value.  If
 * additional functionality is required it is advised to review the `LayoutHandler` annotation and associated
 * `LayoutHandlerDelegate` interface.
 *
 * @see LayoutHandler
 * @see org.androidtransfuse.layout.LayoutHandlerDelegate
 *
 * @author John Ericksen
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Layout {

    /**
     * Layout resource to use as the content view.
     */
    int value();
}
