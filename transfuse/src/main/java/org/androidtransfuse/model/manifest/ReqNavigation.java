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
package org.androidtransfuse.model.manifest;

import org.androidtransfuse.annotations.LabeledEnum;

public enum ReqNavigation implements LabeledEnum {
    UNDEFINED("undefined"),
    NONAV("nonav"),
    DPAD("dpad"),
    TRACKBALL("trackball"),
    WHEEL("wheel");

    private String label;

    private ReqNavigation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}