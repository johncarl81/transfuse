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
package org.androidtransfuse.layout;

/**
 * Interface which defines a manual layout definition call on the Activity in contrast to simply specifying a layout id
 * via the `@Layout` annotation.  A `LayoutHandlerDelegate` may be injected into, most notably the current
 * Activity may be injected allowing a wide variety of view definitions to be specified.  This includes generating the
 * layout problematically or calling a different `setContentView()` method.
 *
 * @author John Ericksen
 */
public interface LayoutHandlerDelegate {

    String INVOKE_LAYOUT_METHOD = "invokeLayout";

    /**
     * Executes during the `onCreate()` method in place of the `setContentView()` call.
     */
    void invokeLayout();
}
