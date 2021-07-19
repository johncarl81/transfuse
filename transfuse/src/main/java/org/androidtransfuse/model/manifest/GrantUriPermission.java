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
 * android:path="string"
 * android:pathPattern="string"
 * android:pathPrefix="string"
 *
 * @author John Ericksen
 */
public class GrantUriPermission extends ManifestBase {

    private String path;
    private String pathPattern;
    private String pathPrefix;

    @XmlAttribute(name = "path", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @XmlAttribute(name = "pathPattern", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getPathPattern() {
        return pathPattern;
    }

    public void setPathPattern(String pathPattern) {
        this.pathPattern = pathPattern;
    }

    @XmlAttribute(name = "pathPrefix", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }
}
