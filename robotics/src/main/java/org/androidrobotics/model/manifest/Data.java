package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
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

    /*
    android:host="string"
      android:mimeType="string"
      android:path="string"
      android:pathPattern="string"
      android:pathPrefix="string"
      android:port="string"
      android:scheme="string"
     */
}
