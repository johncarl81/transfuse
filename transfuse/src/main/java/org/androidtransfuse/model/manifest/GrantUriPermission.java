package org.androidtransfuse.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * attributes:
 * android:path="string"
 * android:pathPattern="string"
 * android:pathPrefix="string"
 *
 * @author John Ericksen
 */
public class GrantUriPermission {

    @XStreamAlias("android:path")
    @XStreamAsAttribute
    private String path;
    @XStreamAlias("android:pathPattern")
    @XStreamAsAttribute
    private String pathPattern;
    @XStreamAlias("android:pathPrefix")
    @XStreamAsAttribute
    private String pathPrefix;

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
}
