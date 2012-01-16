package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author John Ericksen
 */
public class UsesSDK {

    @XStreamAlias("android:minSdkVersion")
    @XStreamAsAttribute
    private Integer minSdkVersion;
    @XStreamAlias("android:targetSdkVersion")
    @XStreamAsAttribute
    private Integer targetSdkVersion;
    @XStreamAlias("android:maxSdkVersion")
    @XStreamAsAttribute
    private Integer maxSdkVersion;

    /*
    android:minSdkVersion="integer"
          android:targetSdkVersion="integer"
          android:maxSdkVersion="integer"
     */
}
