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
package org.androidtransfuse.listeners;

/**
 * Defines the `Activity` `onBackPressed()` Call-Through method.
 *
 * Each defined method represents a method in the corresponding `Activity` class.
 *
 * *Only one Call-Through component per type may be defined per injection graph.*
 *
 * This can be used instead of `@OnBackPressed` if you need more control over where `super.onBackPressed()` is called.
 *
 * To actually call `super.onBackPressed()` you will need a method like this in your `Activity` super class:
 *
 *  `class BaseActivity { public void superBack() { super.onBackPressed(); } }`
 *
 * and don't forget to use that super in your annotated component:
 *
 *  `@Activity(type=BaseActivity.class)`
 *
 *
 * @author Dan Bachelder
 */
public interface ActivityOnBackPressedListener {

    @CallThrough
    void onBackPressed();
}
