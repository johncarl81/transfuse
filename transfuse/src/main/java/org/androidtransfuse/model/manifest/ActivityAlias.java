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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * attributes
 * android:enabled=["true" | "false"]
 * android:exported=["true" | "false"]
 * android:icon="drawable resource"
 * android:label="string resource"
 * android:name="string"
 * android:permission="string"
 * android:targetActivity="string"
 * <p/>
 * can contain:
 * <intent-filter>
 * <meta-data>
 *
 * @author John Ericksen
 */
public class ActivityAlias {

    private Boolean enabled;
    private Boolean exported;
    private String icon;
    private String label;
    private String name;
    private String permission;
    private String targetActivity;
    private List<IntentFilter> intentFilters = new ArrayList<IntentFilter>();
    private List<MetaData> metaData = new ArrayList<MetaData>();

    @XmlAttribute(name = "enabled", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @XmlAttribute(name = "exported", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getExported() {
        return exported;
    }

    public void setExported(Boolean exported) {
        this.exported = exported;
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

    @XmlAttribute(name = "permission", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @XmlAttribute(name = "targetActivity", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getTargetActivity() {
        return targetActivity;
    }

    public void setTargetActivity(String targetActivity) {
        this.targetActivity = targetActivity;
    }

    @XmlElement(name = "intent-filter")
    public List<IntentFilter> getIntentFilters() {
        return intentFilters;
    }

    public void setIntentFilters(List<IntentFilter> intentFilters) {
        this.intentFilters = intentFilters;
    }

    @XmlElement(name = "meta-data")
    public List<MetaData> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<MetaData> metaData) {
        this.metaData = metaData;
    }
}
