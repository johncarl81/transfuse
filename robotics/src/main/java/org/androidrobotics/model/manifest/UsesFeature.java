package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * attributes:
 * android:name="string"
 * android:required=["true" | "false"]
 * android:glEsVersion="integer"
 *
 * @author John Ericksen
 */
public class UsesFeature {

    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;
    @XStreamAlias("android:required")
    @XStreamAsAttribute
    private Boolean required;
    @XStreamAlias("android:glEsVersion")
    @XStreamAsAttribute
    private Integer glEsVersion;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Integer getGlEsVersion() {
        return glEsVersion;
    }

    public void setGlEsVersion(Integer glEsVersion) {
        this.glEsVersion = glEsVersion;
    }
}
