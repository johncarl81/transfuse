package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
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

    /*
    android:name="string"
           android:resource="resource specification"
           android:value="string"
     */
}
