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

import jakarta.xml.bind.annotation.XmlAttribute;


/**
 * attributes:
 * android:description="string resource"
 * android:icon="drawable resource"
 * android:label="string resource"
 * android:name="string"
 *
 * @author John Ericksen
 */
public class PermissionGroup extends ManifestBase {

    private String description;
    private String icon;
    private String label;
    private String name;

    @XmlAttribute(name = "description", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlAttribute(name = "icon", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @XmlAttribute(name = "label", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlAttribute(name = "name", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
