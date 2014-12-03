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

import org.androidtransfuse.model.Identified;
import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.processor.Merge;
import org.androidtransfuse.processor.MergeCollection;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

/**
 * attributes:
 * android:enabled=["true" | "false"]
 * android:exported=["true" | "false"]
 * android:icon="drawable resource"
 * android:label="string resource"
 * android:name="string"
 * android:permission="string"
 * android:process="string"
 * 
 * can contain:
 * <intent-filter>
 * <meta-data>
 *
 * @author John Ericksen
 */
public class Service extends Mergeable implements Identified {

    private Boolean enabled;
    private Boolean exported;
    private String icon;
    private String label;
    private String name;
    private String permission;
    private String process;
    private List<IntentFilter> intentFilters = new ArrayList<IntentFilter>();
    private List<MetaData> metaData = new ArrayList<MetaData>();

    @Merge("e")
    @XmlAttribute(name = "enabled", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Merge("x")
    @XmlAttribute(name = "exported", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getExported() {
        return exported;
    }

    public void setExported(Boolean exported) {
        this.exported = exported;
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

    @Merge("p")
    @XmlAttribute(name = "permission", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Merge("r")
    @XmlAttribute(name = "process", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    @MergeCollection(collectionType = ArrayList.class, type = IntentFilter.class)
    @XmlElement(name = "intent-filter")
    public List<IntentFilter> getIntentFilters() {
        return intentFilters;
    }

    public void setIntentFilters(List<IntentFilter> intentFilters) {
        this.intentFilters = intentFilters;
    }

    @MergeCollection(collectionType = ArrayList.class, type = MetaData.class)
    @XmlElement(name = "meta-data")
    public List<MetaData> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<MetaData> metaData) {
        this.metaData = metaData;
    }

    @Override
    @XmlTransient
    public String getIdentifier() {
        return name;
    }

    public void updatePackage(String manifestPackage){
        if(name != null && name.startsWith(manifestPackage) && containsTag("n")){
            name = name.substring(manifestPackage.length());
        }
    }
}
