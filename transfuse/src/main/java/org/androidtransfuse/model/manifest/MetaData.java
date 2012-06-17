package org.androidtransfuse.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.androidtransfuse.model.Mergeable;
import org.androidtransfuse.processor.Merge;

/**
 * attributes:
 * android:name="string"
 * android:resource="resource specification"
 * android:value="string"
 *
 * @author John Ericksen
 */
public class MetaData extends Mergeable {

    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;
    @XStreamAlias("android:resourceSpecification")
    @XStreamAsAttribute
    private String resourceSpecification;
    @XStreamAlias("android:value")
    @XStreamAsAttribute
    private String value;

    @Merge("n")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Merge("s")
    public String getResourceSpecification() {
        return resourceSpecification;
    }

    public void setResourceSpecification(String resourceSpecification) {
        this.resourceSpecification = resourceSpecification;
    }

    @Merge("v")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getIdentifier() {
        return "metaData";
    }
}
