package org.androidrobotics.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author John Ericksen
 */
public class Permission {

    @XStreamAlias("android:description")
    @XStreamAsAttribute
    private String description;
    @XStreamAlias("android:icon")
    @XStreamAsAttribute
    private String icon;
    @XStreamAlias("android:label")
    @XStreamAsAttribute
    private String label;
    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;
    @XStreamAlias("android:permissionGroup")
    @XStreamAsAttribute
    private String permissionGroup;
    @XStreamAlias("android:protectionLevel")
    @XStreamAsAttribute
    private ProtectionLevel protectionLevel;

    /*
    android:description="string resource"
            android:icon="drawable resource"
            android:label="string resource"
            android:name="string"
            android:permissionGroup="string"
            android:protectionLevel=["normal" | "dangerous" |
                                     "signature" | "signatureOrSystem"]
     */
}
