/**
 * Copyright 2012 John Ericksen
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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.androidtransfuse.model.Identified;
import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.processor.Merge;

/**
 * attributes:
 * android:name
 *
 * @author John Ericksen
 */
public class Action extends Mergeable implements Identified {

    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;

    public String getName() {
        return name;
    }

    @Merge("n")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIdentifier() {
        return name;
    }
}
