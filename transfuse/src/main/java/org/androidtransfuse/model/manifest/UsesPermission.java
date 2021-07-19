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
package org.androidtransfuse.model.manifest;

import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.processor.Merge;

import jakarta.xml.bind.annotation.XmlAttribute;

/**
 * attributes:
 * android:name="string"
 * android:maxSdkVersion="integer"
 *
 * @author John Ericksen
 */
public class UsesPermission extends Mergeable implements Comparable<UsesPermission> {

    private String name;
    private Integer maxSdkVersion;

    @Merge("n")
    @XmlAttribute(name = "name", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Merge("v")
    @XmlAttribute(name = "maxSdkVersion", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Integer getMaxSdkVersion() {
        return maxSdkVersion;
    }

    public void setMaxSdkVersion(Integer maxSdkVersion) {
        this.maxSdkVersion = maxSdkVersion;
    }

    @Override
    public int compareTo(UsesPermission usesPermission) {
        return getName().compareTo(usesPermission.getName());
    }
}
