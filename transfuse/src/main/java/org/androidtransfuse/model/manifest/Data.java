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
 * android:host="string"
 * android:mimeType="string"
 * android:path="string"
 * android:pathPattern="string"
 * android:pathPrefix="string"
 * android:port="string"
 * android:scheme="string"
 *
 * @author John Ericksen
 */
public class Data {

    private String host;
    private String mimeType;
    private String path;
    private String pathPattern;
    private String pathPrefix;
    private String port;
    private String scheme;

    @XmlAttribute(name = "host", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @XmlAttribute(name = "mimeType", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

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

    @XmlAttribute(name = "port", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @XmlAttribute(name = "scheme", namespace = ManifestNamespaceMapper.ANDROID_URI)
    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
}
