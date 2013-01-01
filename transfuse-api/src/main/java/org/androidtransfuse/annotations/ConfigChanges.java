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

public enum ConfigChanges implements LabeledEnum {
    MCC("mcc"),
    MNC("mnc"),
    LOCALE("locale"),
    TOUCHSCREEN("touchscreen"),
    KEYBOARD("keyboard"),
    KEYBOARD_HIDDEN("keyboardHidden"),
    NAVIGATION("navigation"),
    SCREEN_LAYOUT("screenLayout"),
    FONT_SCALE("fontScale"),
    UI_MODE("uiMode"),
    ORIENTATION("orientation"),
    SCREEN_SIZE("screenSize"),
    SMALLEST_SCREENSIZE("smallestScreenSize");

    private final String label;

    private ConfigChanges(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}


    




