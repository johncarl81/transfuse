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
 * attributes:
 * android:authorities="list"
 * android:enabled=["true" | "false"]
 * android:exported=["true" | "false"]
 * android:grantUriPermissions=["true" | "false"]
 * android:icon="drawable resource"
 * android:initOrder="integer"
 * android:label="string resource"
 * android:multiprocess=["true" | "false"]
 * android:name="string"
 * android:permission="string"
 * android:process="string"
 * android:readPermission="string"
 * android:syncable=["true" | "false"]
 * android:writePermission="string"
 *
 * can contain:
 * <meta-data>
 * <grant-uri-permission>
 * <path-permission>
 *
 * @author John Ericksen
 */
public class Provider extends ManifestBase {

    private String authorities;
    private Boolean enabled;
    private Boolean exported;
    private Boolean grantUriPermissions;
    private String icon;
    private Integer initOrder;
    private String label;
    private Boolean multiprocess;
    private String name;
    private String permission;
    private String process;
    private String readPermission;
    private Boolean syncable;
    private String writePermission;
    private List<MetaData> metaData = new ArrayList<MetaData>();
    private List<GrantUriPermission> grantUriPermissionList = new ArrayList<GrantUriPermission>();
    private List<PathPermission> pathPermissions = new ArrayList<PathPermission>();

    @XmlAttribute(name = "authorities", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

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

    @XmlAttribute(name = "grantUriPermissions", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getGrantUriPermissions() {
        return grantUriPermissions;
    }

    public void setGrantUriPermissions(Boolean grantUriPermissions) {
        this.grantUriPermissions = grantUriPermissions;
    }

    @XmlAttribute(name = "icon", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @XmlAttribute(name = "initOrder", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Integer getInitOrder() {
        return initOrder;
    }

    public void setInitOrder(Integer initOrder) {
        this.initOrder = initOrder;
    }

    @XmlAttribute(name = "label", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlAttribute(name = "multiprocess", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getMultiprocess() {
        return multiprocess;
    }

    public void setMultiprocess(Boolean multiprocess) {
        this.multiprocess = multiprocess;
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

    @XmlAttribute(name = "process", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    @XmlAttribute(name = "readPermission", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getReadPermission() {
        return readPermission;
    }

    public void setReadPermission(String readPermission) {
        this.readPermission = readPermission;
    }

    @XmlAttribute(name = "syncable", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public Boolean getSyncable() {
        return syncable;
    }

    public void setSyncable(Boolean syncable) {
        this.syncable = syncable;
    }

    @XmlAttribute(name = "writePermission", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getWritePermission() {
        return writePermission;
    }

    public void setWritePermission(String writePermission) {
        this.writePermission = writePermission;
    }

    @XmlElement(name = "meta-data")
    public List<MetaData> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<MetaData> metaData) {
        this.metaData = metaData;
    }

    @XmlElement(name = "grant-uri-permission")
    public List<GrantUriPermission> getGrantUriPermissionList() {
        return grantUriPermissionList;
    }

    public void setGrantUriPermissionList(List<GrantUriPermission> grantUriPermissionList) {
        this.grantUriPermissionList = grantUriPermissionList;
    }

    @XmlElement(name = "path-permission")
    public List<PathPermission> getPathPermissions() {
        return pathPermissions;
    }

    public void setPathPermissions(List<PathPermission> pathPermissions) {
        this.pathPermissions = pathPermissions;
    }
}
