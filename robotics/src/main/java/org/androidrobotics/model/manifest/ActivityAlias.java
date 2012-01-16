package org.androidrobotics.model.manifest;

/**
 * attributes
 * android:enabled=["true" | "false"]
 * android:exported=["true" | "false"]
 * android:icon="drawable resource"
 * android:label="string resource"
 * android:name="string"
 * android:permission="string"
 * android:targetActivity="string"
 * <p/>
 * can contain:
 * <intent-filter>
 * <meta-data>
 *
 * @author John Ericksen
 */
public class ActivityAlias {

    private Boolean enabled;
    private Boolean exported;
    private String icon;
    private String label;
    private String name;
    private String permission;
    private String targetActivity;
}
