package org.androidrobotics.model.manifest;

/**
 * @author John Ericksen
 */
public class Permission {
    private String description;
    private String icon;
    private String label;
    private String name;
    private String permissionGroup;
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
