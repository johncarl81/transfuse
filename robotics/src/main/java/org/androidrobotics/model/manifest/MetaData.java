package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * attributes:
 * android:name="string"
 * android:resource="resource specification"
 * android:value="string"
 *
 * @author John Ericksen
 */
public class MetaData {

    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;
    @XStreamAlias("android:resourceSpecification")
    @XStreamAsAttribute
    private String resourceSpecification;
    @XStreamAlias("android:value")
    @XStreamAsAttribute
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceSpecification() {
        return resourceSpecification;
    }

    public void setResourceSpecification(String resourceSpecification) {
        this.resourceSpecification = resourceSpecification;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
