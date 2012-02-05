package org.androidtransfuse.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * attributes:
 * android:minSdkVersion="integer"
 * android:targetSdkVersion="integer"
 * android:maxSdkVersion="integer"
 *
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

    public Integer getMinSdkVersion() {
        return minSdkVersion;
    }

    public void setMinSdkVersion(Integer minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }

    public Integer getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setTargetSdkVersion(Integer targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    public Integer getMaxSdkVersion() {
        return maxSdkVersion;
    }

    public void setMaxSdkVersion(Integer maxSdkVersion) {
        this.maxSdkVersion = maxSdkVersion;
    }
}
