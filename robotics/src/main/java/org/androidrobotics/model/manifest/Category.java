package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * attributes
 * android:name="string"
 *
 * @author John Ericksen
 */
public class Category {

    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;
}
