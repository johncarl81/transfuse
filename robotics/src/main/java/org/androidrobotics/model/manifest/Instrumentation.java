package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author John Ericksen
 */
public class Instrumentation {

    @XStreamAlias("android:functionalTest")
    @XStreamAsAttribute
    private Boolean functionalTest;
    @XStreamAlias("android:handleProfiling")
    @XStreamAsAttribute
    private Boolean handleProfiling;
    @XStreamAlias("android:icon")
    @XStreamAsAttribute
    private String icon;
    @XStreamAlias("android:label")
    @XStreamAsAttribute
    private String label;
    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;
    @XStreamAlias("android:targetPackage")
    @XStreamAsAttribute
    private String targetPackage;

    /*
    android:functionalTest=["true" | "false"]
                 android:handleProfiling=["true" | "false"]
                 android:icon="drawable resource"
                 android:label="string resource"
                 android:name="string"
                 android:targetPackage="string"
     */
}
