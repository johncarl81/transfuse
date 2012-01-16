package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * attributes:
 * android:name
 *
 * @author John Ericksen
 */
public class Action {

    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;
    /*

     */
}
