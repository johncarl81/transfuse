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

/**
 * Constants representing the android:windowSoftInputMode property on the Activity Manifest tag.
 *
 * @author John Ericksen
 */
public enum WindowSoftInputMode implements Labeled {

    STATE_UNSPECIFIED("stateUnspecified"),
    STATE_UNCHANGED("stateUnchanged"),
    STATE_HIDDEN("stateHidden"),
    STATE_ALWAYS_HIDDEN("stateAlwaysHidden"),
    STATE_VISIBLE("stateVisible"),
    STATE_ALWAYS_VISIBLE("stateAlwaysVisible"),
    ADJUST_UNSPECIFIED("adjustUnspecified"),
    ADJUST_RESIZE("adjustResize"),
    ADJUST_PAN("adjustPan");

    private final String label;

    private WindowSoftInputMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}