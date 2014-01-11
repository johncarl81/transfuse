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

import org.androidtransfuse.annotations.ProtectionLevel;
import org.androidtransfuse.model.Identified;
import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.processor.Merge;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * attributes:
 * android:description="string resource"
 * android:icon="drawable resource"
 * android:label="string resource"
 * android:name="string"
 * android:permissionGroup="string"
 * android:protectionLevel=["normal" | "dangerous" |
 * "signature" | "signatureOrSystem"]
 *
 * @author John Ericksen
 */
public class Permission extends Mergeable implements Identified {

    private String description;
    private String icon;
    private String label;
    private String name;
    private String permissionGroup;
    private ProtectionLevel protectionLevel;

    @Merge("d")
    @XmlAttribute(name = "description", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Merge("i")
    @XmlAttribute(name = "icon", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Merge("l")
    @XmlAttribute(name = "label", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    @Merge("n")
    @XmlAttribute(name = "name", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Merge("g")
    @XmlAttribute(name = "permissionGroup", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getPermissionGroup() {
        return permissionGroup;
    }

    public void setPermissionGroup(String permissionGroup) {
        this.permissionGroup = permissionGroup;
    }

    @Merge("p")
    @XmlAttribute(name = "protectionLevel", namespace = ManifestNamespaceMapper.ANDROID_URI)
    @XmlJavaTypeAdapter(LabeledConverter.ProtectionLevelConverter.class)
    public ProtectionLevel getProtectionLevel() {
        return protectionLevel;
    }

    public void setProtectionLevel(ProtectionLevel protectionLevel) {
        this.protectionLevel = protectionLevel;
    }

    @XmlTransient
    public String getIdentifier(){
        return name;
    }
}
