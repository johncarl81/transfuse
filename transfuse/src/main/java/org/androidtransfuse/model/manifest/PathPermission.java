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


/**
 * attributes:
 * android:path="string"
 * android:pathPrefix="string"
 * android:pathPattern="string"
 * android:permission="string"
 * android:readPermission="string"
 * android:writePermission="string"
 *
 * @author John Ericksen
 */
public class PathPermission extends ManifestBase {

    private String path;
    private String pathPrefix;
    private String pathPattern;
    private String permission;
    private String readPermission;
    private String writePermission;

    @XmlAttribute(name = "path", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @XmlAttribute(name = "pathPrefix", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    @XmlAttribute(name = "pathPattern", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getPathPattern() {
        return pathPattern;
    }

    public void setPathPattern(String pathPattern) {
        this.pathPattern = pathPattern;
    }

    @XmlAttribute(name = "permission", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @XmlAttribute(name = "readPermission", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getReadPermission() {
        return readPermission;
    }

    public void setReadPermission(String readPermission) {
        this.readPermission = readPermission;
    }

    @XmlAttribute(name = "writePermission", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getWritePermission() {
        return writePermission;
    }

    public void setWritePermission(String writePermission) {
        this.writePermission = writePermission;
    }
}
