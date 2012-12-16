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

    @XStreamAlias("android:host")
    @XStreamAsAttribute
    private String host;
    @XStreamAlias("android:mimeType")
    @XStreamAsAttribute
    private String mimeType;
    @XStreamAlias("android:path")
    @XStreamAsAttribute
    private String path;
    @XStreamAlias("android:pathPattern")
    @XStreamAsAttribute
    private String pathPattern;
    @XStreamAlias("android:pathPrefix")
    @XStreamAsAttribute
    private String pathPrefix;
    @XStreamAlias("android:port")
    @XStreamAsAttribute
    private String port;
    @XStreamAlias("android:scheme")
    @XStreamAsAttribute
    private String scheme;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathPattern() {
        return pathPattern;
    }

    public void setPathPattern(String pathPattern) {
        this.pathPattern = pathPattern;
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
}
