package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author John Ericksen
 */
public class UsesPermission {

    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;

    /*
    android:name="string"
     */

    public String getName() {
        return name;
    }
}
